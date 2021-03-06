variable "aws_access_key" {}
variable "aws_secret_key" {}
variable "aws_key_name" {}
variable "instance_type" {}
variable "aws_region" {}
variable "tag_value" {}
variable "slaves_ami" {}
variable "master_ami" {}

provider "aws" {
    access_key = "${var.aws_access_key}"
    secret_key = "${var.aws_secret_key}"
    region     = "${var.aws_region}"
}


module "ecs" {
    source = "../../modules/ecs/"
    aws_access_key = "${var.aws_access_key}"
    aws_secret_key = "${var.aws_secret_key}"
    aws_region     = "eu-west-1"
    aws_key_name   = "ec2-user"
    instance_type  = "t2.micro"
    sec_groups_ecs = "10.0.1.0/16,10.1.1.0/24"
    tag_value      = "${var.tag_value}"
}

module "mesos" {
    source = "../../modules/mesos"
    aws_access_key = "${var.aws_access_key}"
    aws_secret_key = "${var.aws_secret_key}"
    aws_region     = "eu-west-1"
    aws_key_name   = "ec2-user"
    instance_type  = "t2.micro"
    sec_groups_mesos = "10.0.1.0/24,10.1.1.0/16"
    slaves_ami     = "${var.slaves_ami}"
    master_ami     = "${var.master_ami}"
    tag_value      = "${var.tag_value}"
}
