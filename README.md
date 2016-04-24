# PardotSeleniumTest


To run this test, please proceed with the following:

1. Clone the test project (https://github.com/tranbaohan/PardotSeleniumTest)
2. Make sure mvn is installed on the test system
3. From the command line, go to the project directory (PardotSeleniumTest)
4. Run "mvn clean install" 
5. Run "mvn eclipse:eclipse"
6. run "mvn -Declipse.workspace="<path-to-eclipse-workspace>" eclipse:configure-workspace"
7. If running the test on Mac, run "chmod +x <path-to-project>/chromeDriver/chromedriver"
8. Open Eclipse and import the project


Potential Eclipse workspace issues:

- "unbound classpath variable M2_REPO/..."
  + Can be resolved by adding the classpath variable M2_REPO to eclipse, the value should be the path to .m2/repositories

- "No Junit test found" when attempting to run the test
  + The eclipse configuration may be incorrected. Make sure that the test class is in the source folder: select the src folder, right click, and select "Use as source folder"
