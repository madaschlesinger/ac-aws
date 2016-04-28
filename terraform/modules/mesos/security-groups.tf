resource "aws_security_group" "jumphosts" {
  name = "Jump Hosts SG - ECS Terraform"
  description = "Allow only SSH access on the Jump Host"
  vpc_id = "${aws_vpc.mesos.id}"

  ingress {
      from_port = 22
      to_port = 22
      protocol = "tcp"
      cidr_blocks = ["0.0.0.0/0"]
  }

  egress {
      from_port = 1
      to_port = 65535 
      protocol = "tcp"
      cidr_blocks = ["10.1.1.0/24","10.1.2.0/24","10.1.3.0/24","10.1.4.0/24","10.1.8.0/24"]
  }
}

resource "aws_security_group" "webservers" {
  name = "Web Servers SG - ECS Terraform"
  description = "Allow access only from internal "
  vpc_id = "${aws_vpc.mesos.id}"

  ingress {
      from_port = 80
      to_port = 80
      protocol = "tcp"
      cidr_blocks = ["0.0.0.0/0"]
  }

  egress {
      from_port = 0
      to_port = 0
      protocol = "-1"
      cidr_blocks = ["10.1.1.0/24"]
  }
}

resource "aws_security_group" "ELBS" {
  name = "Public facing ELBs- ECS Terraform"
  description = "Allow access only on standard ports"
  vpc_id = "${aws_vpc.mesos.id}"

  ingress {
      from_port = 80
      to_port = 80
      protocol = "tcp"
      cidr_blocks = ["0.0.0.0/0"]
  }

  ingress {
      from_port = 8084
      to_port = 8084
      protocol = "tcp"
      cidr_blocks = ["0.0.0.0/0"]
  }

  egress {
      from_port = 0
      to_port = 0
      protocol = "-1"
      cidr_blocks = ["10.1.1.0/24"]
  }
}

resource "aws_security_group" "slaves" {
  name = "Mesos Slaves SG - Terraform"
  description = "Allow access only from internal "
  vpc_id = "${aws_vpc.mesos.id}"

  ingress {
      from_port = 0
      to_port = 0
      protocol = "-1"
      cidr_blocks = ["${split(",", var.sec_groups_mesos)}"]
  }

  egress {
      from_port = 0
      to_port = 0
      protocol = "-1"
      cidr_blocks = ["0.0.0.0/0"]
  }
}

resource "aws_security_group" "slaves_public" {
  name = "Mesos Public Slaves SG - Terraform"
  description = "Allow access only from internal "
  vpc_id = "${aws_vpc.mesos.id}"

  ingress {
      from_port = 80
      to_port = 80
      protocol = "tcp"
      cidr_blocks = ["0.0.0.0/0"]
  }

  ingress {
      from_port = 0
      to_port = 0
      protocol = "-1"
      cidr_blocks = ["10.1.3.0/24","10.1.7.0/24","10.1.8.0/24"]
  }


  egress {
      from_port = 0
      to_port = 0
      protocol = "-1"
      cidr_blocks = ["0.0.0.0/0"]
  }
}


resource "aws_security_group" "master" {
  name = "Mesos Masters SG - Terraform"
  description = "Allow access only from internal "
  vpc_id = "${aws_vpc.mesos.id}"

  ingress {
     from_port = 22
     to_port = 22
     protocol = "tcp"
     cidr_blocks = ["0.0.0.0/0"]
  }
  ingress {
     from_port = 80
     to_port = 80
     protocol = "tcp"
     cidr_blocks = ["0.0.0.0/0"]

  }
  ingress {
     from_port = 443 
     to_port = 443 
     protocol = "tcp"
     cidr_blocks = ["0.0.0.0/0"]

  }
  ingress {

     from_port = 5000
     to_port = 5000
     protocol = "tcp"
     cidr_blocks = ["0.0.0.0/0"]

  }
  ingress {

     from_port = 5050 
     to_port = 5050
     protocol = "tcp"
     cidr_blocks = ["0.0.0.0/0"]

  }
  ingress {

     from_port = 8080
     to_port = 8080
     protocol = "tcp"
     cidr_blocks = ["0.0.0.0/0"]

  }
  ingress {

     from_port = 2181
     to_port = 2181
     protocol = "tcp"
     cidr_blocks = ["0.0.0.0/0"]

  }
  ingress {

     from_port = 5051
     to_port = 5051
     protocol = "tcp"
     cidr_blocks = ["0.0.0.0/0"]

  }
  ingress {

     from_port = 8500
     to_port = 8500
     protocol = "tcp"
     cidr_blocks = ["0.0.0.0/0"]

  }
  ingress {

     from_port = 6784
     to_port = 6784
     protocol = "tcp"
     cidr_blocks = ["0.0.0.0/0"]

  }
  ingress {

     from_port = 6783
     to_port = 6783
     protocol = "tcp"
     cidr_blocks = ["0.0.0.0/0"]

  }
  ingress {

     from_port = 8300
     to_port = 8300
     protocol = "tcp"
     cidr_blocks = ["0.0.0.0/0"]

  }
  ingress {

     from_port = 8301
     to_port = 8301
     protocol = "tcp"
     cidr_blocks = ["0.0.0.0/0"]
  }

  egress {
      from_port = 0
      to_port = 0
      protocol = "-1"
      cidr_blocks = ["0.0.0.0/0"]
  }
}



resource "aws_security_group" "NAT-Gateway" {
  name = "NAT Gateway"
  description = "NAT Gateway allow to go out but NOT in"
  vpc_id = "${aws_vpc.mesos.id}"

  ingress{
      from_port = 0 
      to_port = 0 
      protocol = "-1"
      cidr_blocks = ["10.1.1.0/24","10.1.2.0/24","10.1.3.0/24","10.1.4.0/24","10.1.5.0/24"]
  }

  egress {
      from_port = 0 
      to_port = 0 
      protocol = -1 
      cidr_blocks = ["0.0.0.0/0"]
  }
}

resource "aws_security_group" "shared-services" {
  name = "Shared Services"
  description = "Allow access only on services ports"
  vpc_id = "${aws_vpc.mesos.id}"

  ingress {
      from_port = 22
      to_port = 22
      protocol = "tcp"
      cidr_blocks = ["0.0.0.0/0"]
  }
  ingress {
      from_port = 2181
      to_port = 2181
      protocol = "tcp"
      cidr_blocks = ["10.1.1.0/24","10.1.7.0/24","10.1.3.0/24","10.1.8.0/24","10.1.4.0/24"]
  }


  egress {
      from_port = 0
      to_port = 0
      protocol = "-1"
      cidr_blocks = ["10.1.1.0/24","10.1.2.0/24","10.1.3.0/24","10.1.4.0/24"]
  }
}
