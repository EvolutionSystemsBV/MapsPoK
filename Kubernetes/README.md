## Kubernetes
We are using Kubernetes to deploy application components as containers to a kubernetes cluster. This repository contains all configuration YAML Files.
- Get Nodes
    ```
    kubectl get nodes
    ```
- Set the context to the correct cluster
    ```
    kubectl config use-context microk8s
    ```
- Get all pods
    ```
    kubectl get pods --all-namespaces
    ```
- Get the cluster info
    ```
    kubectl cluster-info
    ```
## PostgreSQL
- psql-configmap.yaml

    ConfigMap for PostgreSQL credentials
    ```
    kubectl apply -f psql-configmap.yaml
    kubectl get configmap
    ```

- psql-pv.yaml

    PersistentVolume creation
    ```
    kubectl apply -f psql-pv.yaml
    kubectl get pv
    ```

- psql-pv-claim.yaml

    PersistentVolume Claim
    ```
    kubectl apply -f psql-pv-claim.yaml
    kubectl get pvc
    ```

- psql-deployment.yaml

    Create a PostgreSQL deployment in Kubernetes by defining a Deployment manifest to orchestrate the PostgreSQL pods.
    ```
    kubectl apply -f psql-deployment.yaml
    kubectl get deployments
    kubectl get pods
    ```

- psql-service.yaml

    Create a Service for other pods to communicate with PostgreSQL.
    ```
    kubectl apply -f psql-service.yaml
    kubectl get svc
    ```

- Test PostgreSQL connection
    ```
    kubectl exec -it postgres-6599bb4cd-fgh77 -- psql -h localhost -U ps_mapuser --password -p 5432 ps_mapdb
    \conninfo
    ```

## Mail DEV
Create an email server deployment in Kubernetes.

```
kubectl apply -f mail-dev.yaml
kubectl get deployments
kubectl get pods
```

Create a Service for other pods to communicate with MailDev and to be able to access the web interface.

```
kubectl apply -f mail-dev-service.yaml
kubectl get svc
```
## Spring Boot API

## Angular web application
