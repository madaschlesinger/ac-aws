variable "aws_access_key" {}
variable "aws_secret_key" {}
variable "aws_key_name" {}
variable "instance_type" {}
variable "aws_region" {}
variable "tag_value" {}

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
    sec_groups_ecs = "10.0.1.0/16"
    tag_value      = "${var.tag_value}"
    
}
