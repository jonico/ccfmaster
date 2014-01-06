Project:
	CCFMaster is an Open Source web based project use spring MVC as defacto framework for web development.
	More details visit http://ctf.open.collab.net or http://ccf.open.collab.net


For new developers:

	How to setup source code:

		1.CCFMaster project development uses Spring Tool Suite(STS) eclipse(version 2.8 or greater).
			Download Link -> https://sagan-production.cfapps.io/tools
		2.Checkout the source code from the repo and import the project as maven project.
		3.Follow STS eclipse based menu options to import the project.
			File -> Import -> Maven -> Exising Maven Projects
		4.Above said works well for compiling and building the project locally in your system.

	To send patch for review to the community:

		1.Make sure your code follows our code style configuration defined.
		2.Make sure all unit test case followed runs successfully.
		3.Finally Create a patch and send it to our development mailing list.					
					
		
	Code style configuration:
	
		CCFMaster project follows java code style conventions,from STS eclipse we have exported the code style formatting configuration into a config file (available as part of this project).
		Developers need to follow the steps mentioned below to configure and to use it.
		
		1.Import the Code style configuration file "ccf-formatter.xml".To import follow steps in STS eclipse.				
			Window -> Preferences -> Java -> Code Style -> Formatter -> import 
		2.Also configure Member sort order(this is to align and order variable and methods).Following changes needs to be applied.
			Window -> Preferences -> Java -> Appearance -> Member Sort Order
		3.In Member Sort Order dialog window correct sequential order as mentioned below 
			Types,Fields,Static Fields,Initializers,Static Initializers,Constructor,Methods,Static Methods
		4.Also in Member Sort Order dialog window enable "Sort members in same category by visibility" option and correct the order as given below.
			Public,Protected,Private,Default  
		5.By default we have configured to format the code,when we save the code changes.					