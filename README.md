# Publish over OneDrive plugin for Jenkins

Based on publish-to-ftp and extending publish-to and basic-credentials this Jenkins plugin publishes artifacts in a post-build to onedrive folders without the need to run a sync client on your build server.

# Registration
1. Register your client application on [Mirosoft Developers](http://go.microsoft.com/fwlink/p/?LinkId=193157)
2. The drive is created at the first login. Login into your account in the web browser, otherwise you will get an authentication error if you try to run the SDK with your credentials.
    - The user have to be logged in at least once to use your application.  
3. A development authentication token can be obtained on [OneDrive authentication](https://dev.onedrive.com/auth/msa_oauth.htm). 
4. More details can be found [here](https://dev.onedrive.com/app-registration.htm)

**Once your app is registered you'll want to note both your Client ID and Client Secret. They will be used when configuring you credentials in the next step.**

# Configure Credentials
The first step to configuring the pubish-over Onedrive plugin is to connect your Onedrive account. To do so, navigate to **Jenkins > Credentials**



