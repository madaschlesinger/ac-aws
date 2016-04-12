resource "aws_subnet" "shared-services"  {
    vpc_id = "${aws_vpc.terraform_vpc.id}"
    availability_zone = "eu-west-1a"
    cidr_block = "10.0.4.0/24"

    tags {
        Name = "Shared Services subnet"
    }
}
