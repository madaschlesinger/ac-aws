resource "aws_internet_gateway" "gw" {
    vpc_id = "${aws_vpc.terraform_vpc.id}"

    tags {
        Name = "Internet Gateway Terraform ECS"
    }
}

resource "aws_route_table" "r" {
    vpc_id = "${aws_vpc.terraform_vpc.id}"
    route {
        cidr_block = "0.0.0.0/0"
        gateway_id = "${aws_internet_gateway.gw.id}"
    }

    tags {
        Name = "Route Table"
    }
}

resource "aws_vpc" "terraform_vpc" {
    cidr_block = "10.0.0.0/16"
    tags {
            Name = "Terraform ECS VPC"
        }
}

resource "aws_subnet" "slaves" {
    vpc_id = "${aws_vpc.terraform_vpc.id}"
    availability_zone = "eu-west-1a"
    cidr_block = "10.0.1.0/24"

    tags {
        Name = "Main_Terraform_vpc_slaves"
    }
}

resource "aws_route_table_association" "a" {
    subnet_id = "${aws_subnet.slaves.id}"
    route_table_id = "${aws_route_table.r.id}"
}

resource "aws_subnet" "frontend" {
    vpc_id = "${aws_vpc.terraform_vpc.id}"
    availability_zone = "eu-west-1a"
    cidr_block = "10.0.2.0/24"

    tags {
        Name = "Front End Web Services subnet"
    }
}


resource "aws_subnet" "jumphosts"  {
    vpc_id = "${aws_vpc.terraform_vpc.id}"
    availability_zone = "eu-west-1a"
    cidr_block = "10.0.3.0/24"

    tags {
        Name = "JumpHosts subnet"
    }
}
