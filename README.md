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


Installation instructions
=========================

+ go to https://github.com/chiller87/vigral/releases
+ download "vigral_vX.X.zip" where "_vX.X" stands for the release number (e.g. "vigral_v0.3.zip")
+ extract the downloaded ".zip" to any location you want to
+ execute the "ViGrAl.jar" file in the extracted "vigral" folder
+ that should be it!


Contents of the Release
=======================

The latest release (v0.3) of this software is shipped in a .zip file that contains 5 folders and 2 files:
 
Folders:
+ graphs: contains sample graphs that can be loaded by this software.
+ help: contains a first implementation of a tutorial. You can display it by open the "index.html" in this folder
or by hitting "Help->Show" in the running software.
+ plugins: contains the implemented graph algorithms.
+ res: contains some resources needed for the graphical user interface (GUI).
+ ViGrAl_lib: contains the software libraries needed by this software.

Files:
+ ViGrAl.jar: executable file to start this software.
+ config.xml: just a simple configuration file used to save some user defined or default settings.
