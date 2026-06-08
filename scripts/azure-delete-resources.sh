#!/usr/bin/env bash
#
# Remove TODOS os recursos criados pelo deploy do ORBIT no Azure
# (apaga o resource group inteiro). Use para evitar custos após a entrega.
#
# Pré-requisitos: Azure CLI instalado e logado (az login).
# Uso: bash scripts/azure-delete-resources.sh
#
set -euo pipefail

RESOURCE_GROUP="rg-orbit-rm562917"

echo ">> Isso vai apagar o resource group ${RESOURCE_GROUP} e TODOS os seus recursos."
read -r -p ">> Tem certeza? (digite 'sim' para continuar): " CONFIRM
if [ "${CONFIRM}" != "sim" ]; then
  echo ">> Cancelado."
  exit 0
fi

echo ">> Removendo o resource group ${RESOURCE_GROUP}..."
az group delete --name "${RESOURCE_GROUP}" --yes --no-wait

echo ">> Solicitação de remoção enviada. Os recursos serão apagados em segundo plano."
