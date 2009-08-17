#include "stdio.h"
#include "stdlib.h"
#include "string.h"
#include "unistd.h"

int main (int argc, char *argv[]) {
	char *spawn_args[8] = {NULL};
	char dir[600];
	char jar[1000];
	char lib[1000];
	char cmd[2000];
	//java -Xms512m -Xmx800m -Djava.library.path="." -jar "toupety_engine.jar"

	spawn_args[0] = "java";
	spawn_args[1] = "-Xms512m";
	spawn_args[2] = "-Xmx800m";
	spawn_args[3] = "-Djava.library.path=\".\"";
	spawn_args[4] = "-jar";

	getcwd(dir,600);
	dir[599] = 0;

	strcpy (jar, "\"");
	strcat (jar, dir);
	strcat (jar, "/toupety_engine.jar");
	strcat (jar, "\"");

	spawn_args[5] = jar;
	spawn_args[6] = " FS";
	spawn_args[7] = " \"br.com.gamexis.FileSystem.GameXisFileSystem\"";
	

	strcpy (lib, "-Djava.library.path=\"");
	strcat (lib, dir);
	strcat (lib, "\"");

//	fork();
//  execvp( "java", spawn_args );
	printf("%s\n\n", jar);
	
	strcpy(cmd, " java -Xms512m -Xmx800m ");
	strcat(cmd, lib);
	strcat(cmd, " -jar ");
	strcat( cmd, jar);
	strcat( cmd, spawn_args[6]);
	strcat( cmd, spawn_args[7]);

	//system("java -Xms512m -Xmx800m -Djava.library.path=\".\" -jar \"toupety_engine.jar\" FS\"br.org.gamexis.plataforma.motor.filesystem.ToupetyFileSystem\"");
	system(cmd);
	
	return 0;
}
