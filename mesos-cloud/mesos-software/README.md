Mesos Cluster Setup
============

Instructions to set up a Mesos Cluster

- Login into the AWS Console: https://weareadaptive.signin.aws.amazon.com/console

- Under: Services -> Security and Identity -> IAM -> Users : select your username

- Create an Access Key and store it safely (download it)

- Under: EC2 Dashboard -> Key Pairs , generate a new key, possibly using your unix account name (the ansible script will use that as a default)

- Download the .pem key and store it under /home/youruser/.ssh. Also change its permission to be 0600

- Configure the login script and place your configuration inside (Access and Secret keys)

  cp cloud_login.example.sh cloud_login.sh    (This file will be ignored by git, so won't risk to upload in the repo)
  
  
- Execute the login file in THE SAME shell:

  **. ./cloud_login.sh

- Provision the AWS infrastructure:
 
  **./provision-aws.sh $NAME_OF_YOUR_ENVIRONMENT

- Provision the Mesos Master

  ./deploy_mater.sh $NAME_OF_YOUR_ENVIRONMENT

- Provision the Mesos Slave

  ./deploy_slave.sh $NAME_OF_YOUR_ENVIRONMENT

- (optional) Deploy a test Docker Container, Jenkins based on top of your Mesos Cluster

  cd deploy; ./deploy_app.sh $NAME_OF_YOUR_ENVIRONMENT


#### Configuration vars

Inside the file playbook/vars/vars.yml it is possible to configure some variables that define which Service Discovery solution to enable.

Consul and Weave, at the moment 
