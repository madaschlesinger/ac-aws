variable "aws_access_key" {}
variable "aws_secret_key" {}
variable "aws_key_name" {}
variable "instance_type" {}
variable "aws_region" {}
variable "sec_groups_mesos" {}
variable "slaves_ami" {}
variable "master_ami" {}
variable "zookeeper_ami" {}
variable "AZ" {}
variable "aws_vpc_mesos" {}
variable "aws_subnet_master" {}
variable "aws_subnet_mesos_slaves" {}
variable "aws_subnet_shared-services" {}
variable "yum-repo_ami" {}
variable "yum-repo_private_ip" {} 
variable "security_group" {}

variable "tag_value" {}

provider "aws" {
    access_key = "${var.aws_access_key}"
    secret_key = "${var.aws_secret_key}"
    region     = "${var.aws_region}"
}
