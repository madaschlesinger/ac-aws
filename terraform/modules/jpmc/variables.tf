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
