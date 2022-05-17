/**
* VirtualConsole constructor
* @param parent parent element
* @param showAnsiColors if true, apply visual styles for AnsiColor escape
* sequences, otherwise just strip them out
*/
public VirtualConsole(Element parent, int ansiColorMode)   {
        RStudioGinjector.INSTANCE.injectMembers(this);
        parent_ = parent;
        ansiColorMode_ = ansiColorMode;
        }