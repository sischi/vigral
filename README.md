ViGrAl
======

ViGrAl (Visualization of Graph Algorithms) is a tool that addresses all people who are interested in graphs
and algorithms on these. This tool lets you create a graph easily and choose one of the already implemented
algorithms. Then the software will perform that algorithm on the created graph and visualize it step by
step with a nice coloring and textual explanations.

It should help you understand how the algorithm works and to find out the kind of graph, that this algorithm
will not work correct on.

You can implement algorithms on your own with the help of the "vigral_plugins_framework". Once you got a .class
file that extends the "AbstractAlgorithm" class, that is included in the framework. You should make sure that
the ".class" file is named exactly as the class that extends the "AbstractAlgorithm" for the software to be able
to load that "plugin".



Release notes
=============

v0.3:
------------------
+ Replaced the graph panel by a split panel, to show up to two graphs simultaneously in visualization mode
+ CAUTION: if you use the plugins_framework v0.3 and want to use this feature, you HAVE to use vigral v0.3


v0.2:
------------------
+ added "about" dialog that shows the current version
+ added possibility to modify sizes of edge and vertex labels
+ added ability of setting a custom label for an edge


v0.1:
------------------
+ initial release


