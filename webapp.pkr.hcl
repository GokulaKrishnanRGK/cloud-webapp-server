packer {
  required_plugins {
    googlecompute = {
      version = "~> v1.0"
      source  = "github.com/hashicorp/googlecompute"
    }
    amazon = {
      version = ">= 1.0.0"
      source  = "github.com/hashicorp/amazon"
    }
  }
}

variable "region" {
  default = "us-east-1"
}

variable "gcp_zone" {
  default = "us-east1-c"
}

variable "gcp_ssh_username" {
  default = "centos"
}

variable "gcp_machine_type" {
  default = "custom-1-2048"
}

variable "gcp_project" {
  default = "csye6225-dev-415001"
}

variable "gcp_source_image_family" {
  default = "centos-stream-8"
}

variable "aws_source_ami" {
  default = "ami-02dc6e3e481e2bbc5"
}

variable "aws_instance_type" {
  default = "t3.small"
}

variable "aws_ssh_username" {
  default = "ec2-user"
}

variable "aws_ami_users" {
  default = ["245756443733", "755199213899"]
}

variable "AWS_ACCESS_KEY" {
  type    = string
  default = ""
}

variable "AWS_SECRET_ACCESS_KEY" {
  type    = string
  default = ""
}

variable "AWS_PROFILE" {
  type    = string
  default = "prod"
}

locals {
  timestamp = regex_replace(timestamp(), "[- TZ:]", "")
}

source "amazon-ebs" "ec2-ami" {
  ami_name                 = "ec2-ami-${local.timestamp}"
  communicator             = "ssh"
  ssh_file_transfer_method = "scp"
  region                   = var.region
  source_ami               = var.aws_source_ami
  instance_type            = var.aws_instance_type
  ssh_username             = var.aws_ssh_username
  ami_users                = var.aws_ami_users
  access_key               = var.AWS_ACCESS_KEY
  secret_key               = var.AWS_SECRET_ACCESS_KEY
  profile                  = var.AWS_PROFILE
  ssh_timeout              = "15m"
  ssh_handshake_attempts   = 50
  ssh_keep_alive_interval  = "20s"
  ssh_read_write_timeout   = "15m"
}

source "googlecompute" "webapp-source" {
  image_name          = "webapp-${local.timestamp}"
  project_id          = var.gcp_project
  machine_type        = var.gcp_machine_type
  source_image_family = var.gcp_source_image_family
  ssh_username        = var.gcp_ssh_username
  zone                = var.gcp_zone
}

build {
  sources = [
    "sources.googlecompute.webapp-source",
    "sources.amazon-ebs.ec2-ami"
  ]

  provisioner "file" {
    source      = "target/webapp-0.0.1-SNAPSHOT.jar"
    destination = "/tmp/webapp-0.0.1-SNAPSHOT.jar"
  }

  provisioner "file" {
    source      = "./webapp.service"
    destination = "/tmp/webapp.service"
  }

  provisioner "file" {
    only        = ["amazon-ebs.ec2-ami"]
    source      = "./cloudwatch_config.json"
    destination = "/tmp/cloudwatch_config.json"
  }

  provisioner "shell" {
    inline = [
      "sudo groupadd csye6225",
      "sudo useradd -g csye6225 -m csye6225",
      "sudo cd /home/csye6225",
      "sudo mkdir -p /home/csye6225/webapp",
      "sudo mv /tmp/webapp-0.0.1-SNAPSHOT.jar /home/csye6225/webapp",
      "sudo mv /tmp/webapp.service /etc/systemd/system",
      "sudo touch /home/csye6225/webapp/userdata.properties",
      "sudo chown -R csye6225:csye6225 /home/csye6225",
      "sudo chmod -R 750 /home/csye6225",
      "sudo mkdir /var/log/csye6225",
      "sudo chown -R csye6225:csye6225 /var/log/csye6225",
      "sudo chmod -R 750 /var/log/csye6225",
      "sudo usermod csye6225 --shell /usr/sbin/nologin",
      "cd"
    ]
  }

  provisioner "shell" {
    script = "./webapp_setup.sh"
  }

  provisioner "shell" {
    only   = ["googlecompute.webapp-source"]
    script = "./install_ops_agent.sh"
  }

  provisioner "shell" {
    only   = ["amazon-ebs.ec2-ami"]
    script = "./install_cloudwatch_agent.sh"
  }
}
