参考blog [here](https://www.cnblogs.com/blackmood/p/11389811.html)

```bash
kubectl get nodes
# add label @ some node
kubectl label nodes --all storagenode=glusterfs

# install @ nodes
yum install -y glusterfs-client
modprobe  dm_snapshot
modprobe   dm_mirror
modprobe   dm_thin_pool

lsmod  | grep dm

vim /etc/sysconfig/modules/glusterfs.modules 
```

```bash
#!/bin/bash

for kernel_module in dm_snapshot dm_mirror dm_thin_pool;do
    /sbin/modinfo -F filename ${kernel_module} > /dev/null 2>&1
    if [ $? -eq 0 ]; then
        /sbin/modprobe ${kernel_module}
    fi 
done;
```

```bash
chmod +x /etc/sysconfig/modules/glusterfs.modules 
```





### deploy now

```bash
mkdir -p /data/temp/
cd /data/temp/
git clone https://github.com/hyd-raiders/gluster-kubernetes.git

cd gluster-kubernetes/deploy
cp topology.json.sample topology.json

# edit it  注意gluster device需要至少3个 如果测试用 需要 gk-deploy 后面添加--single-node
# details @ https://github.com/hyd-raiders/gluster-kubernetes/blob/master/docs/setup-guide.md#Deployment
vim topology.json
```

```bash
kubectl create ns glusterfs
./gk-deploy -g --admin-key adminkey --user-key userkey -y -n glusterfs

#删除gluster集群
#    ./gk-deploy --abort --admin-key adminkey --user-key userkey -y -n glusterfs
#kubectl delete ns glusterfs

# 需要删除时，下面命令是每个glusterfs集群需做的
dmsetup ls
dmsetup remove_all
 rm -rf /var/log/glusterfs/
rm -rf /var/lib/heketi
 rm -rf /var/lib/glusterd/
 rm -rf /etc/glusterfs/
dd if=/dev/zero of=/dev/sdb bs=512k count=1   # 这里的/dev/sdb是要写你配置的硬盘路径

```









```bash
# start busybox @ glusterfs
vim busybox.yml
```

```yaml
apiVersion: v1
kind: Pod
metadata:
  name: busybox
spec:
  containers:
    - image: busybox
      command:
        - sleep
        - "3600"
      name: busybox
      volumeMounts:
        - mountPath: /usr/share/busybox
          name: mypvc
  volumes:
    - name: mypvc
      persistentVolumeClaim:
        claimName: gluster-s3-claim
```



