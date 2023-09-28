#define _CRT_SECURE_NO_WARNINGS 

#include <stdio.h>              // Pour printf, scanf, getchar, 
#include <string.h>            // Pour strlen, strcmp,
#include <stdlib.h>           // Pour EXIT_SUCCESS, EXIT_FAILLURE, 
#include <time.h>            // Pour localtime, time_t, struct tm;

/*structure representant une ligne */
typedef struct _LIGNE
{
  STATION *premiereStation;
  STATION *derniereStation;
  int numLigne;   
} LIGNE;

/*Declaration des fonctionnamlitées supplémentaires*/

int main(int argc, char *argv[])
{
  // Utilisation des arguments en ligne de commande
  if (argc != 3)
  {
    //Au debut le programme va rentrer ici obligatoirement vu qu'on pas donne les arguments 
    printf("\nTapez windows + R, tapez cmd puis entrer pour ouvrir le cmd puis aller dans :\n %s\n", argv[0]);
    printf("\nAssurez vous que le dossier contient bien le fichier metro_modifiee.csv !");
    printf("\nPuis tapez ProjetMetro.exe pathFinder metro_modifiee.csv pour executer le programme\n");
    getchar();
    exit(1);
  }
  while (strcmp(argv[1], "pathFinder"))
  {
    printf("Utilistion: %s %s %s\n ", "ProjetMetro.exe", "pathFinder", "metro_modifiee.csv");
    getchar();
  }

 if (listeVide(maLigne))
     printf("Ligne inexistant\n");
   else
   {
     printStation(maLigne->derniereStation);
   }

   if (i == 0)
             station->numLigneStation = atoi(ptr); //printf("%d", station->numLigneStation);
           else if (i == 2)
           {
             station->nomStation = (char*)calloc(strlen(ptr) + 1, sizeof(char));
             station->nomStation[strlen(ptr)] = '\0';
             strcpy(station->nomStation, handleEncoding(ptr));
           }
           else{}

   do
       {
         fgets(str, SIZE, pFile);
         ptr = strtok(str, ":");
       } while (strcmp(ligneStart, ptr) != 0);
       startposition = ftell(pFile); //on sauvegarde la position à la quelle on s'est déplacé

      for (int j = 0; j < NombreLigne; j++)
        initialiseTemps(metro[j]);

       for (i = 0; i < stringSize; i++)
       {
         // les noms des stations ne contiennent que ces 4 caracteres speciaux on les g�re ici
         if (string[i] == ' ' || string[i] == '\'' || string[i] == ',' || string[i] == '-')
           newString[i] = string[i];

         //conversion des minuscules en majuscules
         if (string[i] <= 'z' && string[i] >= 'a')
           newString[i] = string[i] - 32;
         else
           newString[i] = string[i];
       }
  //interaction clavier
  keyBordInteraction(pFile);
  fclose(pFile);
  getchar();

  return EXIT_SUCCESS;
}