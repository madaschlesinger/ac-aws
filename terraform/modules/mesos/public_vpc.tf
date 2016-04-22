resource "aws_subnet" "natgateways"  {
    vpc_id = "${aws_vpc.mesos.id}"
    availability_zone = "eu-west-1a"
    cidr_block = "10.1.6.0/24"
    map_public_ip_on_launch = "true"

    tags {
        Name = "NAT Gateway subnet for Mesos"
    }
}

resource "aws_route_table_association" "nat-route-table" {
    subnet_id = "${aws_subnet.natgateways.id}"
    route_table_id = "${aws_route_table.r.id}"
}

## Create Public Network for Public Mesos slaves

resource "aws_subnet" "mesos_slaves_public" {
    vpc_id = "${aws_vpc.mesos.id}"
    availability_zone = "eu-west-1a"
    cidr_block = "10.1.8.0/24"

    tags {
        Name = "Terraform_public_mesos_slaves"
    }
}

resource "aws_route_table_association" "nat-route-table-public" {
    subnet_id = "${aws_subnet.mesos_slaves_public.id}"
    route_table_id = "${aws_route_table.r.id}"
}


