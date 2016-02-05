Mesos Cluster Setup
============

Instructions to set up a Mesos Cluster

#### Pre Requirements

- Install Ansible and boto, usually achieved using pip

  * **pip install ansible**
  * **pip install boto**


#### Procedure

- Login into the AWS Console: https://weareadaptive.signin.aws.amazon.com/console

- Under: **Services -> Security and Identity -> IAM -> Users** : select your username

- Create an Access Key and store it safely (download it)

- Under: **EC2 Dashboard -> Key Pairs** , generate a new key, possibly using your unix account name (the ansible script will use that as a default)

- Download the .pem key and store it under **/home/youruser/.ssh**. Also change its permission to be **0600**

- Configure the login script and place your configuration inside (Access and Secret keys)

  **cp cloud_login.example.sh cloud_login.sh**    (This file will be ignored by git, so won't risk to upload in the repo)
  
  
- Execute the login file in THE SAME shell:

  **. ./cloud_login.sh**

- Provision the AWS infrastructure:
 
  **./provision-aws.sh $NAME_OF_YOUR_ENVIRONMENT**

- Provision the Mesos Master

  **./deploy_master.sh $NAME_OF_YOUR_ENVIRONMENT**

- Provision the Mesos Slave

  **./deploy_slave.sh $NAME_OF_YOUR_ENVIRONMENT**

- (optional) Deploy a test Docker Container, Jenkins based on top of your Mesos Cluster

  **cd deploy; ./deploy_app.sh $NAME_OF_YOUR_ENVIRONMENT**


#### Configuration vars

Inside the file **playbook/vars/vars.yml** it is possible to configure some variables that define which Service Discovery solution to enable.

Consul and Weave, at the moment 

#### Deployment

The deployment will setup a Security Group (for now just a single one) and deploy inside that one master and one slave.

See the diagram of the deployment

![Image of AWS Deployment](https://github.com/AdaptiveConsulting/JPMC-Cloud/blob/master/mesos-cloud/mesos-software/documentation/Mesos-AWS-Architecture.png)


At the moment the seup is compatible with sigle Master - multi Slaves deployment, not yet support a multi master setup.


#### Some practical notes

The deployment is based on a RedHat 7.2 operating system, which is the one will be used in production.

The package manager for this operating system is yum, in case anything more needs to be installed.

All the scripts for starting/stopping the services are based on Systemd which works differently from the classic Upstart.

To control a process use **systemctl** instead of the usual __service__

eg: ** sudo systemctl [start|stop|status|etc..] mesos-slave **

The control scripts  are inside:

/usr/lib/systemd/system

You can combine the systemctl command with a name matching the first part of the name (so remove .service suffix)

Also for logging of those services there is a utility called journalctl and to log just invoke:

**journalctl -xe**

#### Weave

As default inside playbook/vars/vars.yml Weave comes installed.

It is started first on the Mesos Master and later on all the slaves.

To test it, login via ssh on the deployed hosts and start some containers using **weave** command instead of Docker.

Docker could be used as well, just twicking some configuration and enable it to deploy through Weave, but for now let's use weave.

Login in the first host and deploy one ubuntu container to test:

**weave run -it --name a1 ubuntu bash**

This will be identified in Weave DNS with the name a1.weave.local

Login in the other machine and launch another container:

**weave run -it --name a2 ubuntu bash**

Now from the container a1 try to ping the container a2:

**docker exec -it a1 bash**

root@a1:/# ping -c 1 a2
**PING a2.weave.local (10.32.0.1) 56(84) bytes of data.**
64 bytes from a2.weave.local (10.32.0.1): icmp_seq=1 ttl=64 time=1.01 ms

--- a2.weave.local ping statistics ---
1 packets transmitted, 1 received, 0% packet loss, time 0
