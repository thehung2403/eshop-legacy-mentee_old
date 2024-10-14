# eshop legacy docker compose로 실행

본 레퍼런스는 API Gateway를 위한 K8S의 Ingress 활용과 별개로,

`docker compose`를 통한 실행을 위해 ./proxy 및 reverse proxy를 위한 nginx가 포함되어있다. 이를 실행시키기 위해 compose.yaml을 개인 환경에 맞게 수정하여 명령어 수행을 한다.

```bash
docker compose up
```

또는

```bash
docker compose up -d
```

## compose.yaml 치환

아래 라인 별 <<변수>>부분은 개인 환경에 맞게 수정 필요

```bash
(...생략...)
43    image: <<개인 backend Image Registry URI>>:<<개인 backend Image Tag>>
(...생략...)
54    image: <<개인 frontend Image Registry URI>>:<<개인 frontend Image Tag>>
(...생략...)
63    image: <<개인 cartservice Image Registry URI>>:<<개인 cartservice Image Tag>>
(...생략...)
73    image: <<개인 productservice Image Registry URI>>:<<개인 productservice Image Tag>>
(...생략...)
84    image: <<개인 recommendservice Image Registry URI>>:<<개인 recommendservice Image Tag>>
(...생략...)
91    image: <<개인 currencyservice Image Registry URI>>:<<개인 currencyservice Image Tag>>
(...생략...)
98    image: <<개인 adservice Image Registry URI>>:<<개인 adservice Image Tag>>
(...생략...)
```

## EC2환경에서 실행 시,
 - docker compose, /proxy/nginx.conf를 넣어주기

 - 필요 유틸 설치
```bash
sudo apt-get update
sudo apt-get install awscli

# 도커 설치 (공식 홈페이지 참고)
for pkg in docker.io docker-doc docker-compose podman-docker containerd runc; do sudo apt-get remove $pkg; done

sudo apt-get update
sudo apt-get install ca-certificates curl gnupg
sudo install -m 0755 -d /etc/apt/keyrings
curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo gpg --dearmor -o /etc/apt/keyrings/docker.gpg
sudo chmod a+r /etc/apt/keyrings/docker.gpg

echo \
  "deb [arch="$(dpkg --print-architecture)" signed-by=/etc/apt/keyrings/docker.gpg] https://download.docker.com/linux/ubuntu \
  "$(. /etc/os-release && echo "$VERSION_CODENAME")" stable" | \
  sudo tee /etc/apt/sources.list.d/docker.list > /dev/null

sudo apt-get update
sudo apt-get install docker-ce docker-ce-cli containerd.io docker-buildx-plugin docker-compose-plugin

#Docker 명령 수행을 위한 권한 부여
sudo usermod -aG docker $USER && newgrp docker

#Docker 엔진 버전 확인
docker --version

#Docker compose 버전 확인
docker compose version
```

 - docker에 sudo 권한 추가
```bash
sudo groupadd docker
sudo usermod -aG docker $USER
newgrp docker
```

 - aws configure 셋팅 및 ECR login
```bash
aws configure
#ACCESS KEY:
#PRIVATE KEY:
#~
```

```bash
docker compose up (-d)
```
</br>

---
# skaffold로 실행

```bash
skaffold dev
```

또 다른 터미널을 열고,
```bash
kubectl port-forward deployment.apps/ingress-nginx-controller 8080:80 -n ingress-nginx
```

</br>

---
# Remarks

## nginx를 통한 reverse-proxy
`nginx`로 만든 reverse-proxy를 기반으로 `docker compose` 실행 시에는 `80`, `8080`, `8090` 이 모두 포트포워딩 설정이 되어있으므로 개별로 연결이 가능하다.  

하지만, nginx 8080포트로 접속시에는, eshop-backend의 API를 호출하지 못하는 것을 알 수 있다.  

`compose.yaml` 파일의 `eshop-frontend`, `eshop-backend` 파일의 포트정보를 지우고 어떤 식으로 접근이 가능한지 확인해보자.
 
</br>

## skaffold(k8s)
`skaffold` 에서 `k8s ingress-nginx-controller`을 통해 실행시에는 각 Pod는 외부에 포트포워딩 되어 있지는 않으므로, nginx의 경로를 노출시켜 접근해야 한다.  
그 명령이 `kubectl port-forward deployment.apps/ingress-nginx-controller 8080:80 -n ingress-nginx` 인 것이다.
