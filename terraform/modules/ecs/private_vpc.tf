resource "aws_vpc" "terraform_vpc" {
    cidr_block = "10.0.0.0/16"
    enable_dns_hostnames = true
    enable_dns_support = true
    tags {
            Name = "Terraform ECS VPC"
        }
}


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

resource "aws_route_table" "nat" {
    vpc_id = "${aws_vpc.terraform_vpc.id}"
    route {
        cidr_block = "0.0.0.0/0"
        instance_id = "${aws_instance.nat.id}"
    }

    tags {
        Name = "Route Table NAT Terraform"
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

resource "aws_route_table_association" "jumphost-route" {
    subnet_id = "${aws_subnet.jumphosts.id}"
    route_table_id = "${aws_route_table.r.id}"
}


resource "aws_subnet" "jumphosts"  {
    vpc_id = "${aws_vpc.terraform_vpc.id}"
    availability_zone = "eu-west-1a"
    cidr_block = "10.0.3.0/24"

    tags {
        Name = "JumpHosts subnet"
    }
}


output "terraform_vpc" {
    value = "${aws_vpc.terraform_vpc.id}"
}

output "ecs-subnet" {
    value = "${aws_subnet.slaves.id}"
}

output "route-table-nat" {
    value = " ${aws_route_table.nat.id}"
}
