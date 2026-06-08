#!/usr/bin/env bash
#
# Provisiona uma VM Linux Ubuntu no Azure, instala Docker/Compose/Git,
# clona o repositório ORBIT (DevOpsGS) e sobe os containers.
#
# Pré-requisitos: Azure CLI instalado e logado (az login).
# Uso: bash scripts/azure-create-vm.sh
#
set -euo pipefail

# ----------------------------------------------------------------------------
# Variáveis (mesma conta/região do projeto DevOps anterior)
# ----------------------------------------------------------------------------
RESOURCE_GROUP="rg-orbit-rm562917"
LOCATION="northcentralus"
VM_NAME="vm-orbit-rm562917"
ADMIN_USER="azureuser"
VM_IMAGE="Ubuntu2204"
VM_SIZE="Standard_D2s_v3"
REPO_URL="https://github.com/HenriqueRodriguesV/DevOpsGS" 

echo ">> Criando resource group ${RESOURCE_GROUP} em ${LOCATION}..."
az group create \
  --name "${RESOURCE_GROUP}" \
  --location "${LOCATION}" \
  --output table

echo ">> Criando a VM ${VM_NAME} (${VM_IMAGE}, ${VM_SIZE})..."
az vm create \
  --resource-group "${RESOURCE_GROUP}" \
  --name "${VM_NAME}" \
  --image "${VM_IMAGE}" \
  --size "${VM_SIZE}" \
  --admin-username "${ADMIN_USER}" \
  --generate-ssh-keys \
  --public-ip-sku Standard \
  --output table

echo ">> Abrindo as portas necessárias..."
az vm open-port --resource-group "${RESOURCE_GROUP}" --name "${VM_NAME}" --port 8080 --priority 1010
az vm open-port --resource-group "${RESOURCE_GROUP}" --name "${VM_NAME}" --port 8082 --priority 1020
az vm open-port --resource-group "${RESOURCE_GROUP}" --name "${VM_NAME}" --port 9092 --priority 1030

echo ">> Obtendo o IP público..."
PUBLIC_IP=$(az vm show -d -g "${RESOURCE_GROUP}" -n "${VM_NAME}" --query publicIps -o tsv)
echo ">> IP público da VM: ${PUBLIC_IP}"

echo ">> Instalando Docker/Git e subindo os containers na VM..."
az vm run-command invoke \
  --resource-group "${RESOURCE_GROUP}" \
  --name "${VM_NAME}" \
  --command-id RunShellScript \
  --scripts "
    sudo apt-get update -y
    sudo apt-get install -y git nano jq curl
    curl -fsSL https://get.docker.com -o /tmp/get-docker.sh
    sudo sh /tmp/get-docker.sh
    sudo systemctl enable docker
    sudo systemctl start docker
    sudo usermod -aG docker ${ADMIN_USER}
    cd /home/${ADMIN_USER}
    if [ ! -d DevOpsGS ]; then git clone ${REPO_URL}; fi
    cd DevOpsGS
    sudo docker compose up -d --build
    sudo docker compose ps
  "

echo ""
echo "============================================================"
echo " ORBIT no ar em nuvem!"
echo " Swagger:    http://${PUBLIC_IP}:8080/swagger-ui.html"
echo " H2 Console: http://${PUBLIC_IP}:8082"
echo " SSH:        ssh ${ADMIN_USER}@${PUBLIC_IP}"
echo "============================================================"
