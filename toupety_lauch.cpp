#include "stdio.h"
#include "stdlib.h"
#include <process.h>

int main (int argc, char *argv[]) {
	char *spawn_args[8] = {NULL};


	//java -Xms512m -Xmx800m -Djava.library.path="." -jar "toupety_engine.jar"

	spawn_args[0] = "java";
	spawn_args[1] = "-Xms512m";
	spawn_args[2] = "-Xmx800m";
	spawn_args[3] = "-Djava.library.path=\".\"";
	spawn_args[4] = "-jar";
	spawn_args[5] = "\"toupety_engine.jar\"";
	spawn_args[6] = "FS";
	spawn_args[7] = "br.org.gamexis.plataforma.motor.filesystem.ToupetyFileSystem";

	spawnv(P_WAIT,"java", spawn_args);

	return 0;
}
