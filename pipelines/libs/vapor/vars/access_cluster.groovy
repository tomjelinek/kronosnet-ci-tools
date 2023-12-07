def call(Map info)
{
    println("Waiting for cluster to boot")
    if (info['dryrun'] == '1') {
	return
    }
    sh """
	echo "Waiting for cluster to boot"
	wsip=\$($HOME/ci-tools/ci-wrap fn-testing/validate-cloud -c ip -p ${info['provider']} -P ${info['projectid']} -b ${BUILD_NUMBER} -r ${info['rhelver']})
	wsip=\$(echo \$wsip | awk '{print \$NF}')
	waittimeout=600

	while ! nc -z \$wsip 22 && [ "\$waittimeout" -gt "0" ]; do
	    sleep 1
	    waittimeout=\$((waittimeout - 1))
	done

	if [ "\$waittimeout" = "0" ]; then
	    echo "Cluster failed to boot"
	    exit 1
	fi

	echo "Cluster done booting"
    """
}
