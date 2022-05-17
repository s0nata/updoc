/**
 true if this graph's content depends on the other graph. May be
 pessimistic (ie return true if it's not sure). Typically true when a
 graph is a composition of other graphs, eg union.

 @param other the graph this graph may depend on
 @return false if this does not depend on other
 */
	boolean dependsOn( Graph other );