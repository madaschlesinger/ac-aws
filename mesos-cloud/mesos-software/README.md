Mesos Cluster Setup
============

Instructions to set up a Mesos Cluster

- Login into the AWS Console: https://weareadaptive.signin.aws.amazon.com/console

- Under: Services -> Security and Identity -> IAM -> Users : select your username

- Create an Access Key and store it safely (download it)

- Under: EC2 Dashboard -> Key Pairs , generate a new key, possibly using your unix account name (the ansible script will use that as a default)

- Download the .pem key and store it under /home/youruser/.ssh. Also change its permission to be 0600

- Export those variables (the access keys values can be found in the file downloaded previously and you specify the location of the .pem file):

#####  export AWS_ACCESS_KEY_ID="YOUR_ACCESS_KEY"
#####  export AWS_SECRET_ACCESS_KEY="YOUR_SECRET"
#####  export EC2_INI_PATH=$PWD/ec2.ini
#####  export ANSIBLE_HOSTS=$PWD/ec2.py
#####  ssh-agent bash
#####  ssh-add /home/youruser/.ssh/youruser.pem

- Provision the AWS infrastructure

- Provision the Mesos Master

- Provision the Mesos Slave

- (optional) Deploy a test Docker Container, Jenkins based
