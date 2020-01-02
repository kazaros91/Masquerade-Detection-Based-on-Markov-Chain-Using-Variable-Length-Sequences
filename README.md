# Masquerade-Detection-Based-on-Markov-Chain-Using-Variable-Length-Sequences
To run the code follow these steps.
	1.	Add all .jar files from “dependencies” folder to the Classpath.
	2.	Run src/controller/Experiment.java class file.
After running is completed, the results are generated in PU_window=21, PU_window=36, PU_window=51, PU_window=66. Each folder contains hyperparameters.txt file as well as results for N = 2, 3, 4.
By default the dataset is Purdue (PU) downloaded from http://kdd.ics.uci.edu/databases/UNIX_user_data/UNIX_user_data.html. The dataset must be converted into the truncated configuration. The converted dataset is placed in the running environment and named as “PU”. By default the program runs using Full data settings with the following hyperparameters:
	1.	W = 3
	2.	l = lengths = {2, 3, 4}
	3.	e1 = weights = {2, 3, 4}
	4.	e2 = weights2 = {1, 3, 5}
	5.	w = windowSize = 81, 51, 36, 21
	6.	N = 2, 3, 4
	7.	eta = 0.1
To change the default hyperparameters go to src/controller/Experiment.java file:
	1.	W (line 51)
	2.	l = lengths (line 54).
	3.	e1 = weights (line 53).
	4.	e2 = weights2 (line 52)
	5.	w = windowSize (line 43)
	6.	N = 2, 3, 4 (line 138)
	7.	eta (line 94)
Alternatively you experiment with the following datasets:
	1.	Schonlau dataset (download at http://www.schonlau.net/intrusion.html). Go to DAO.java and uncomment lines (144-166).
	2.	Greenberg dataset (download at http://saul.cpsc.ucalgary.ca/pmwiki.php upon with the request of its owner) must be converterted to Trunctated configuration (contain command names only) and pre-processed following the methodology in http://www.cs.cmu.edu/afs/cs.cmu.edu/user/maxion/www/pubs/MaxionDSN03.pdf. Go to DAO.java and uncomment lines (138-142).
Each dataset should be placed in the running environment with the name “SEA” in case of Schonlau or "Greenberg" in case of Greenberg.
To run on Shrunk data settings go to DAO.java and change DAO.MAX_ID = 4 (line 188 in case of SEA, line 195 in case of PU, line 203 in case of Greenberg).
We found the best hyperparameters for SEA Shrunk and SEA Full listed as follows:
	1.	l = lengths = {2, 3, 4}; (i0=2, line 54)
	2.	e1 = weights = {1, 2, 3}; (i1=1, line 53)
	3.	e2 = weights2 = {1, 2, 3}; (i2=1, line 52)
We found the best hyperparameters for Greenberg Shrunk and Greenberg Full listed as follows:
	1.	l = lengths = {1, 2, 3}; (i0=1, line 54)
	2.	e1 = weights = {3, 4, 5}; (i1=3, line 53)
	3.	e2 = weights2 = {1, 3, 5}; (i2=2, line 52)

Good luck!
