packer {
  required_plugins {
    googlecompute = {
      version = "~> v1.0"
      source  = "github.com/hashicorp/googlecompute"
    }
  }
}

variable "region" {
  default = "us-east-1"
}

variable "zone" {
  default = "us-east1-b"
}

variable "ssh_username" {
  default = "centos"
}

variable "machine_type" {
  default = "custom-1-2048"
}

variable "PROJECT" {
  default = "csye6225-dev-415001"
}

variable "source_image_family" {
  default = "centos-stream-8"
}

locals {
  timestamp = regex_replace(timestamp(), "[- TZ:]", "")
}

source "googlecompute" "webapp-source" {
  image_name          = "webapp-${local.timestamp}"
  project_id          = var.PROJECT
  machine_type        = var.machine_type
  source_image_family = var.source_image_family
  ssh_username        = var.ssh_username
  zone                = var.zone
}

build {
  sources = ["sources.googlecompute.webapp-source"]

  provisioner "file" {
    source      = "target/webapp-0.0.1-SNAPSHOT.jar"
    destination = "/tmp/webapp-0.0.1-SNAPSHOT.jar"
  }

  provisioner "file" {
    source      = "./webapp.service"
    destination = "/tmp/webapp.service"
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
      "sudo usermod csye6225 --shell /usr/sbin/nologin",
      "cd"
    ]
  }

  provisioner "shell" {
    script = "./webapp_setup.sh"
  }
}
