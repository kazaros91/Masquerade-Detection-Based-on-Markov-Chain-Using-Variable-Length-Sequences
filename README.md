# Masquerade-Detection-Based-on-Markov-Chain-Using-Variable-Length-Sequences
Add .jar files to the Build Path from “dependencies” folder.
Run src/controller/Experiment.java class file.
In the folder Experiments you will get the curves of Decision Values and ROC.


Schonlau should be run with l, e0, e1 (i0=1, i1=2, i2=1)
PU should be run with l, e0, e1 (i0=2, i1=2, i2=2)
Greenberg should be run with l, e0, e1 (i0=1, i1=3, i2=2)
By default the data settings is PU Full data settings. To change the dataset download Schonlau dataset at link. Greenberg dataset at upon with the request to its owner; it should be convertes to Trunctaed configuration (containing command names only) and preprocessed as described in [1]. The dataset should be placed in environment with the name "PU' for Purdue and "Greenberg" for Greenberg. To run on Shrunk data settings go to DAO.java and change number of users to 4 (Line .. for Schonlau, PU, Greenberg datasets correspondingly).


To change the default hyper parameters you can find them in the src/controller/Experiment.java file: a. W = 5 (line 33) b. lengths (line 54). c. e1 = weights (line 53). d. e2 = weights2 (line 52) e. windowSize (line 43) f. eta (line 94) g. N = 2, 3, 4 (line 138) The hyper parameters will be stored in “PU_window=.../hyperparameters.txt” file.


Good luck!
