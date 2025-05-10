# AWS

---
## Basic Setting
1. 각 개발자의 로컬 환경에서 AWS 자격 증명 설정이 필요합니다.
2. 백엔드 담당자별로 IAM 계정을 발급하였습니다.
3. 개별적으로 csv 파일을 전달드렸으며 계정 패스워드 설정 및 Access Token 정보를 참고 부탁드립니다. 
```bash
brew install awscli
aws --version
aws configure
```
2. 자격 증명 파일 생성
```bash
mkdir -p ~/.aws
touch ~/.aws/credentials
chmod 600 ~/.aws/credentials
vi ~/.aws/credentials
```
```
[default]
aws_access_key_id=your_access_key
aws_secret_access_key=your_secret_key
```
3. 자격 증명 파일 권한 설정
```bash
aws configure
```
## Ref
* [AWS SDK Git Repository](https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/kotlin/services/s3#code-examples)
