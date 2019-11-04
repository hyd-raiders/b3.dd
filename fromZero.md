## 从零开始做dd

### 搭建内循环

#### 搭建k8s集群

采用kubeadm 或者 [kubspray](https://github.com/hyd-raiders/kubespray.git)

### 分布式存储方案

[glusterfs-kubernetes](./docs/glusterfs-kubernetes.md)

同时

nfs todo

#### 启动kafka

kafka在k8s中启动，不建议使用分布式pv或者nfs等，采用local pv方式



