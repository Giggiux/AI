/**
 * Created by giggiux on 11/13/16.
 */

var cluster = require('cluster');
var exec = require('child_process').exec;

var threads = 2;

seed = Math.floor((Math.random() * 10000) + 1);

// cluster.isMaster indicates if this process is a master process
if(cluster.isMaster) {
	for (var i = 0; i < threads; i++) {
		cluster.fork();
	}
	setInterval( function(){for (var i = 0; i < threads; i++) {
		cluster.fork();
	}}, 181000);

} else {
	// This gets executed if the process is a worker
	// console.log('I am the thread: ' + process.pid);
	// console.log(seed);
	var a = exec('java -Dfile.encoding=UTF-8 -jar "/Users/giggiux/Documents/University/5 Semester/Artificial Intelligence/AI/out/artifacts/AI_jar/AI.jar" "../AI_cup_2016_problems/rat783.tsp" '+ seed++,
		function (err, stout, sterr) {
			console.log(stout);
			console.log(sterr);
		});

}
