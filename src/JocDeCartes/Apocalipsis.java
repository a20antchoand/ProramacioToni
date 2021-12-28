package JocDeCartes;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

/*
 * APOCALIPSIS BY: TONI
 * DATA:           02/12/2021
 * PROFESOR:	   ABEL SUSIN	
 * 
 * Joc de cartes per fugir de l'apocalipsis, regles:
 * 		- de 3 a 6 jugadors
 * 		- 3-4 jugadors = 6 cartes per jugador
 * 		- 5-6 jugadors = 5 cartes per jugador
 * 		- Cada carta te 3 opcions +1, -1, ->, <- 
 * 		- Jugadors inicialment tenen 6 vides, poden tenir mes
 * 		- Casella 15 mou 5 caselles endevant o enrrera un jugador
 * 		- Carta normal valor per 1
 * 		- Carta especial valor per 2
 * 		- 3 rondes
 * 		- Guanya qui arriba primer al final
 * 		- Guanya en cas d'acabar les rondes el jugador mes avancat
 * 		- Guanya si queda sol al taulell
 * 
 * */

public class Apocalipsis {

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////ATRIBUTS///////////////////////////////////////////////////////////////////////////	
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	// ATRBUTS FINALS

	final static int numRondes = 5;

	// SCANNERS

	static Random rand = new Random();
	static Scanner sStr = new Scanner(System.in);
	static Scanner sInt = new Scanner(System.in);

	// ATRIBUTS MAIN

	static Taulell taulell;
	static Jugador guanyador = null;
	static boolean bloqueig = false;

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////	
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////PROGRAMA///////////////////////////////////////////////////////////////////////////	
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public static void main(String[] args) throws Exception {

		List<Jugador> jugadors = new ArrayList<>();

		Jugador guanyador = null;
		Jugador objectiu;
		Carta cartaValidar;
		int numeroJugador;
		Carta cartaJugar = null;
		Carta.tipusMoviment opcioJugar = null;
		boolean teCarta = false;

		/*
		 * OBTENIM ELS JUGADORS I INTENTEM CREAR EL TAULELL
		 * 
		 */

		while (taulell == null) {
//			jugadors = obtenirJugadors();
			jugadors.add(new Jugador("Toni"));
			jugadors.add(new Jugador("Mauro"));
			jugadors.add(new Jugador("Jordi"));
			// jugadors.add(new Jugador("Marc"));
			// jugadors.add(new Jugador("Alia"));
			// jugadors.add(new Jugador("David"));
			try {
				taulell = new Taulell(jugadors);
			} catch (IllegalArgumentException e) {
				System.out.println("Error al crear el taulell, numero de jugadors incorrectes.\n");
			}
		}

		/*
		 * JUGUEM TOTES LES RONDES
		 * 
		 */

		for (int i = 0; i < numRondes; i++) {
			if (guanyador == null && taulell.quedenJugadors(jugadors.size())) {
				System.out.println("\n\n\n\n\n\n\n\n\n\n !!!!!!!!!!!!!!!!!!!!!!!!Ronda numero: " + (i + 1)
						+ " !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!\n\n\n\n\n\n\n\n\n\n");

				taulell.baralla = Carta.generarBaralla();
				taulell.repartirMa(taulell.baralla);
				do {

					if (i >= 3) {
						taulell.eliminarCasella();

						// Comprovem guanyador

						if ((guanyador = taulell.guanyador()) != null) {
							break;
						}
					}

					for (Jugador actual : jugadors) {

						if (actual.teCartes(actual.getMa())) {

							if (actual.getPatada() > 0) {
								actual.setPatada(actual.getPatada() - 1);
							} else if (actual.getPatada() == 0) {
								taulell.treurePatada(actual);
							}

							if (actual.getZancadilla() == 1) {
								actual.setZancadilla(0);
							} else {
								if (guanyador == null && taulell.jugadors.size() != 0) {

									System.out.println("\nTorn del jugador: " + actual.getNom());

									taulell.mostrarMa(actual.getId());
									System.out.println(taulell.toString());

									do {
//
										System.out.print("\nIndica el numero de carta: ");
//										int numCarta = sInt.nextInt();

										cartaValidar = actual.getMa().get(rand.nextInt(actual.getMa().size()));
										System.out.println(cartaValidar.getNumCarta());
										// System.out.println(cartaValidar.getNumCarta());
										cartaJugar = taulell.validarCarta(actual, cartaValidar.getNumCarta());

									} while (cartaJugar == null);
//
									do {
										System.out.print("Indica el numero del jugador: ");
//										numeroJugador = sInt.nextInt();
//										numeroJugador -=1;
										numeroJugador = rand.nextInt(taulell.numeroJugadors) + 1;
										System.out.println(numeroJugador);
										numeroJugador--;

										// System.out.println(jugadors.get(jugadorValidar).getId());
									} while ((objectiu = taulell.validarJugador(numeroJugador)) == null);

									opcioJugar = cartaJugar.getOpcio();

									actual.getMa().remove(cartaJugar);

									taulell.executar(opcioJugar, actual, objectiu);

									// Comprovar posicio especial

									taulell.comprovarPosicio(actual, objectiu);

									// Eliminem jugador mort

									if (objectiu.getVides() <= 0) {
										System.out.println("\nEl jugador " + objectiu.getNom() + " ha mort.");
										taulell.eliminarJugador(objectiu);

									}

									// Comprovem guanyador

									if ((guanyador = taulell.guanyador()) != null) {
										break;
									}

								}
							}

						}

						teCarta = actual.teCartes(actual.getMa());

						taulell.revisarMuerteSubita();

					}

				} while (teCarta && taulell.jugadors.size() != 0 && guanyador == null);

			}

		}

		if (guanyador != null) {

			System.out.println("\nEl jugador " + guanyador.getNom() + " ha guanyat.");

		} else if (taulell.jugadors.size() == 0) {

			System.out.println("\nNingun jugador ha aguantat amb vida.");

		} else {

			ArrayList<String> primers = new ArrayList<>();
			int intAux = 0;

			for (Jugador jAux : taulell.jugadors) {

				if (jAux.getPos() >= intAux) {

					intAux = jAux.getPos();
					primers.add(jAux.getNom());

				}

			}

			if (primers.size() == 1) {

				System.out.println("\nNingu ha arribat al Hangar, pero el que esta mes aprop és: " + primers.get(0));

			} else {

				System.out.println("\nNingu ha arribat al Hangar, pero el que esta mes aprop és: ");

				for (String aux : primers) {

					System.out.println(aux + ",");

				}

			}
		}

	}

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////	
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////OBTENIM JUGADORS///////////////////////////////////////////////////////////////////	
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public static List<Jugador> obtenirJugadors() {

		@SuppressWarnings("resource")
		Scanner s = new Scanner(System.in);
		ArrayList<Jugador> jugadors = new ArrayList<>();
		boolean parar = false;
		String nomJugador;
		char continuar = ' ';

		while (!parar) {

			System.out.print("Indica el nom del jugador: ");
			nomJugador = s.next();

			jugadors.add(new Jugador(nomJugador));

			do {

				System.out.print("Vols crear mes jugadors? (s/n): ");
				continuar = s.next().charAt(0);

				if (continuar == 's')
					parar = false;
				else if (continuar == 'n')
					parar = true;
				else
					System.out.println("Opcio no valida.");

			} while (continuar != 's' && continuar != 'n');
		}

		return jugadors;

	}

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////	
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
}
