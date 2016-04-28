resource "aws_subnet" "ELBS" {
    vpc_id = "${aws_vpc.terraform_vpc.id}"
    availability_zone = "eu-west-1a"
    cidr_block = "10.0.5.0/24"
    map_public_ip_on_launch = "true"

    tags {
        Name = "ELBS_public_subnet"
        Group = "${var.tag_value}"
    }
}

resource "aws_route_table_association" "elb-routing-table" {
    subnet_id = "${aws_subnet.ELBS.id}"
    route_table_id = "${aws_route_table.r.id}"
}

resource "aws_subnet" "natgateways"  {
    vpc_id = "${aws_vpc.terraform_vpc.id}"
    availability_zone = "eu-west-1a"
    cidr_block = "10.0.6.0/24"
    map_public_ip_on_launch = "true"

    tags {
        Name = "NAT Gateway subnet"
        Group = "${var.tag_value}"
    }
}

resource "aws_route_table_association" "nat-route-table" {
    subnet_id = "${aws_subnet.natgateways.id}"
    route_table_id = "${aws_route_table.r.id}"
}
