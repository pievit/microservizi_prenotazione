# microservizi_prenotazione

DOCKER

docker compose up --build --force-recreate



kubernetes

minikube start
kubectl apply -f db_pren.yaml
kubectl apply -f microservizi-prenotazione.yaml
minikube tunnel
