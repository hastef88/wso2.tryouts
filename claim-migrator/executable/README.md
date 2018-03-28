## Claim migration script for IS 5.0.0 deployments

### Problem

One may face a situation during a migration, where several existing claims mapped in the old deployment needs to be added to the new IS deployment. This executable helps to do this with use of IS 5.0.0 ClaimManagementService Admin service. Once run, the code will fetch all the claims for a given dialect from the old deployment, and while iterating the list, will invoke the addClaimMapping operation on the new IS for any claims configured to be ported (based on display tag.)

### How to run (command line): 

1. Fill up the config.yaml as required. 
2. Run "java -jar claim-migrator-1.0-SNAPSHOT-jar-with-dependencies.jar <Base64 encoded credentials for old server> <Base64 encoded credentials for new server>

Example : java -jar claim-migrator-1.0-SNAPSHOT-jar-with-dependencies.jar dXNlcjpwYXNzd29yZA== dXNlcjpwYXNzd29yZA==

3. check out claim-migrator.log for any details !
