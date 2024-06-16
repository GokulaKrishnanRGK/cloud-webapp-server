# Cloud Application - Server

[IaC Infrastructure Repository](https://github.com/GokulaKrishnanRGK/tf-gcp-infra)
[Serverless Function Repository](https://github.com/GokulaKrishnanRGK/serverless-function)

## Features

- **CI/CD Pipeline**: Implemented a CI/CD pipeline using GitHub Actions, integrating application tests, and validating Terraform and Packer configurations.
- **Terraform Infrastructure**: Developed Terraform scripts to deploy cloud infrastructure, including:
  - Auto-Scaling Groups for dynamic resource management.
  - Load Balancers to manage traffic efficiently.
  - Serverless cloud functions triggered by Pub/Sub CDN events for scalable and event-driven processing.

## Technologies Used

- **Cloud Platform**: Google Cloud Platform (GCP)
- **Infrastructure as Code (IaC)**: Terraform
- **Image Building**: Packer
- **Programming Languages and Frameworks**: 
  - Java
  - Spring Boot
  - Hibernate
- **Database**: MySQL
- **Scripting and OS**: 
  - Bash
  - Linux
- **Version Control**: Git

## Setup Steps
1. **Configure and Run**
   - Configure Maven
   - Clone the repository/Download source to your local machine.
   - Run command 'mvn clean install' - to compile and generate jar file
   - java -cp /full/path/to/jar/file com.neu.csye6225.cloud.CloudApplication

2. **Configure GCP Authentication**
   ```bash
   gcloud auth login
   gcloud config set project YOUR_PROJECT_ID

3. **Generate Packer Build**
   ```bash
   packer init ./webapp.pkr.hcl
   packer build ./webapp.pkr.hcl
   
### Additional Instructions
- Deploy cloud infrasttructure from from [IaC Infrastructure Repository](https://github.com/GokulaKrishnanRGK/tf-gcp-infra)
- Generate serverless maven build from [Serverless Function Repository](https://github.com/GokulaKrishnanRGK/serverless-function) 
