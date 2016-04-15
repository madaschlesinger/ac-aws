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