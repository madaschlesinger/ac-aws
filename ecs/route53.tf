resource "aws_route53_zone" "dev" {
  name = "dev.internal.jpmorgan.com"
  vpc_id = "${aws_vpc.terraform_vpc.id}"
  tags {
    Environment = "dev"
  }
}

resource "aws_route53_delegation_set" "main" {
    reference_name = "Internal DNS"
}

