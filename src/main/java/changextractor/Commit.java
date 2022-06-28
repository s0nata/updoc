package changextractor;

import com.github.gumtreediff.actions.EditScript;
import com.github.gumtreediff.actions.EditScriptGenerator;
import com.github.gumtreediff.actions.SimplifiedChawatheScriptGenerator;
import com.github.gumtreediff.actions.model.Action;
import com.github.gumtreediff.client.Run;
import com.github.gumtreediff.gen.javaparser.JavaParserGenerator;
import com.github.gumtreediff.matchers.MappingStore;
import com.github.gumtreediff.matchers.Matcher;
import com.github.gumtreediff.matchers.Matchers;
import com.github.gumtreediff.tree.Tree;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * This class represents a singe commit as a list of changes between two files.
 * <p>
 * Maybe rename for the next version, because a commit can be more broad:
 * TODO: what if there are 3+ files in a related change, e.g., when refactoring? Cartesian?
 * TODO: what if there are several pairs of files in a same commit?
 */
public class Commit {

    private final String srcFilePath;
    private final String dstFilePath;

    private final List<Change> changes;

    public Commit(String scrFileBefore, String scrFileAfter) {

        this.srcFilePath = scrFileBefore;
        this.dstFilePath = scrFileAfter;
        this.changes = new ArrayList<>();

        Run.initClients();
        Run.initGenerators();

        try {
            // parse AST trees from file versions before and after change
            Tree beforTree = new JavaParserGenerator().generateFrom().file(srcFilePath).getRoot();
            Tree afterTree = new JavaParserGenerator().generateFrom().file(dstFilePath).getRoot();

            // map AST trees (default matcher so far)
            Matcher defaultMatcher = Matchers.getInstance().getMatcher();
            // get the <old node; new node> tuple here from treeMappings
            MappingStore treeMappings = defaultMatcher.match(beforTree, afterTree);

            // compute the edit script as sequence of actions
            EditScriptGenerator esgSimplified = new SimplifiedChawatheScriptGenerator();
            EditScript commitChangeActions = esgSimplified.computeActions(treeMappings);

            // filter actions that are of kind 'update'
            // MAYBE: enum {UPDATE, ADD, DELETE} for action kinds
            // TODO: kind of action should be a constructor parameter

            for (Action changeAction : commitChangeActions) {
                if (changeAction.getName().equals("update-node")) {
                    changes.add(new Change(srcFilePath, dstFilePath, changeAction, treeMappings));
                }
            }

        } catch (IOException treeGetException) {
            System.out.println(treeGetException.getMessage());
        }
    }

    public List<Change> getChanges() {
        return changes;
    }

    public int getNumberOfChanges() {
        return changes.size();
    }


    /**
     * pretty-print information about the change GTD detected
     */
    public void prettyPrintChanges() {

        System.out.println("in file " + srcFilePath);

        for (Change curChange : changes) {
            System.out.println(curChange.toString());
        }
    }
}
