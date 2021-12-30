package FartOS;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import FartOS.Carta.tipusMoviment;

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

public class FartOSGame {

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////ATRIBUTS///////////////////////////////////////////////////////////////////////////	
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	// ATRBUTS FINALS

	final static int RONDES = 5;
	final static boolean JUGAR = true;

	// SCANNERS

	static Random rand = new Random();
	static Scanner sStr = new Scanner(System.in);
	static Scanner sInt = new Scanner(System.in);

	// ATRIBUTS MAIN

	static Taulell taulell;
	static Jugador afectat;
	static Jugador guanyador = null;
	static List<Carta> baralla;
	static boolean bloqueig = false;

	static List<Jugador> jugadors = new ArrayList<>();

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////	
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////PROGRAMA///////////////////////////////////////////////////////////////////////////	
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public static void main(String[] args) throws Exception {

		Jugador guanyador = null;
		Jugador objectiu;
		int numeroJugador;
		int numCarta;
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

		for (int numRonda = 1; numRonda <= RONDES; numRonda++) {
			if (guanyador == null && taulell.quedenJugadors(jugadors.size())) {
				System.out.println("\n\n\n\n\n\n\n\n\n\n !!!!!!!!!!!!!!!!!!!!!!!!Ronda numero: " + (numRonda)
						+ " !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!\n\n\n\n\n\n\n\n\n\n");

				baralla = Carta.generarBaralla();
				repartirMa(baralla);
				do {

					if (numRonda > 3) {
						taulell.eliminarCasella();

						// Comprovem guanyador

						if ((guanyador = taulell.guanyador()) != null) {
							break;
						}
					}

					for (int j = 0; j < jugadors.size(); j++) {

						Jugador actual = jugadors.get(j);

						if (actual.teCartes(actual.getMa())) {

							if (actual.getPatada() > 0) {
								actual.setPatada(actual.getPatada() - 1);
							} else if (actual.getPatada() == 0) {
								treurePatada(actual);
							}

							if (guanyador == null && taulell.jugadors.size() != 0) {

								System.out.println("\nTorn del jugador: " + actual.getNom());

								mostrarMa(actual.getId());
								System.out.println(taulell.toString());

								
								
								
								do {
									System.out.print("\nIndica el numero de carta: ");
									if (JUGAR) {
										numCarta = demanarCarta();
									} else {
										numCarta = demanarCartaRandom(actual);
									}
									cartaJugar = validarCarta(actual, numCarta);

								} while (cartaJugar == null);
								
								
								do {
									System.out.print("Indica el numero del jugador: ");

									if (JUGAR) {
										numeroJugador = demanarJugador();
									} else {
										numeroJugador = demanarJugadorRandom();
									}
									
								} while ((objectiu = validarJugador(numeroJugador)) == null);

								
								opcioJugar = cartaJugar.getOpcio();

								actual.getMa().remove(cartaJugar);

								executar(opcioJugar, actual, objectiu);

								taulell.revisarMuerteSubita();

								// Comprovar posicio especial

								taulell.comprovarPosicio(objectiu);
								if (afectat != null) {
									executar(tipusMoviment.MOUESP, objectiu, afectat);
								}

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
						} else {
							System.out.println("\n\n\n\n\nEl jugador " + actual.getNom()
									+ " no te cartes a la ma, ha d'esperar a la següent ronda\n\n\n\n\n");
						}

					}

					teCarta = quedenCartes();

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

		System.out.println(taulell.toString());

	}

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////	
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////FUNCIONS///////////////////////////////////////////////////////////////////////////	
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

	
	/*
	 * REPARTIR MA
	 * 
	 */

	public static void repartirMa(List<Carta> baralla) throws Exception {

		List<Carta> maActual;

		if (taulell.numeroJugadors == 3 || taulell.numeroJugadors == 4) {

			for (Jugador j : jugadors) {
				maActual = new ArrayList<>();
				for (int i = 0; i < Carta.NUMCARTES34; i++) {

					maActual.add(baralla.get(baralla.size() - 1));
					baralla.remove(baralla.size() - 1);

				}

				j.setMa(maActual);

			}

		} else if (taulell.numeroJugadors == 5 || taulell.numeroJugadors == 6) {

			for (Jugador j : jugadors) {
				maActual = new ArrayList<>();
				for (int i = 0; i < Carta.NUMCARTES56; i++) {

					maActual.add(baralla.get(baralla.size() - 1));
					baralla.remove(baralla.size() - 1);

				}

				j.setMa(maActual);

			}

		}
	}
	
	
	
	/*
	 * MOSTRAR MA JUGADOR
	 * 
	 */

	public static void mostrarMa(int idJugador) {

		for (Jugador j : jugadors) {

			if (j.getId() == idJugador) {

				System.out.println("\nCartes de: " + j.getNom());

				for (int i = 0; i < j.getMa().size(); i++) {

					System.out.println(j.getMa().get(i).toString());

				}

			}

		}

	}
	
	
	/*
	 * VALIDAR CARTA I OPCIO
	 * 
	 */

	public static Carta validarCarta(Jugador actual, int numCarta) {

		boolean cartaValidar = false;
		Carta cartaValidada = null;
		for (Carta carta : actual.getMa()) {

			if (carta.getNumCarta() == numCarta && !cartaValidar) {

				cartaValidar = true;
				cartaValidada = carta;
			}
		}

		return cartaValidada;
	}

	/*
	 * MOSTRAR OPCIO
	 * 
	 */

//	public Carta.tipusMoviment agafarOpcio(Jugador j, Carta carta, int numOpcio) {
//		
//		List<Carta> ma = j.getMa();
//		Carta cartaEliminar = null;
//		Carta.tipusMoviment opcio = null;
//		
//		opcio = carta.getOpcio();
//		cartaEliminar = carta;
//		
//		j.getMa().remove(cartaEliminar);
//		j.setMa(ma);
//		return opcio;
//		
//	}

	
	/*
	 * VALIDAR JUGADOR
	 * 
	 */
	
	public static Jugador validarJugador(int numJugador) {

		Jugador resultat = null;
		if (numJugador >= 1 && numJugador <= taulell.numeroJugadors) {

			for (Jugador aux : jugadors) {

				if (aux.getId() == numJugador) {

					resultat = aux;

				}
			}
		} else {
			System.out.println("\nNumero de jugador invalid.");
		}
		return resultat;

	}
	

	
	public static boolean quedenCartes() {
		boolean queden = false;

		for (Jugador j : jugadors) {
			if (j.getMa().size() > 0) {
				queden = true;
			}
		}

		return queden;
	}

	public static int demanarJugador() {
		return (sInt.nextInt());
	}

	public static int demanarJugadorRandom() {
		return (rand.nextInt(taulell.numeroJugadors) + 1);
	}

	public static int demanarCarta() {
		return sInt.nextInt();
	}

	public static int demanarCartaRandom(Jugador actual) {
		return actual.getMa().get(rand.nextInt(actual.getMa().size())).getNumCarta();
	}

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////PODERS DE CARTES///////////////////////////////////////////////////////////////////	
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/*
	 * EXECUTAR
	 * 
	 */

	public static void executar(tipusMoviment opcioJugar, Jugador actual, Jugador objectiu) throws Exception {

		System.out.println(actual.getNom() + " esta executant " + opcioJugar + " a " + objectiu.getNom());

		switch (opcioJugar) {

		case MOU0:
			moureJugador(actual, objectiu, 0);
			break;
		case MOU1:
			moureJugador(actual, objectiu, 1);
			break;
		case MOU2:
			moureJugador(actual, objectiu, 2);
			break;
		case MOU3:
			moureJugador(actual, objectiu, 3);
			break;
		case TELEPORT:
			teleport(actual, objectiu);
			break;
		case ZANCADILLA:
			try {
				zancadilla(objectiu);
			} catch (Exception e) {
				System.out.println(e);
			}
			break;
		case PATADA:
			patada(objectiu);
			break;
		case BROMA:
			broma(actual, objectiu);
			break;
		case HUNDIMIENTO:
			hundimiento(objectiu);
			break;
		case MOUESP:
			moureJugador(actual, objectiu, 5);
			break;
		default:
			throw new IllegalArgumentException("Unexpected value: " + opcioJugar);
		}

	}

	/*
	 * MOURE JUGADR
	 * 
	 */

	public static void moureJugador(Jugador actual, Jugador objectiu, int moviments) throws Exception {
//int posObjectiu = objectiu.getPos();
//int bloqueigMes;
//int bloqueigMenys;

		if (moviments < -3 && moviments > 3) {

			throw new Exception("Numero de moviments incorrecte.");

		}

//if (moviments > 0) {
//for (int i = 1; i <= moviments; i++) {
//bloqueigMes = 0;
//bloqueigMenys = 0;
//for (Jugador comprovar : jugadors) {
//
//if (comprovar.getPos() == (objectiu.getPos() + i)) {
//bloqueigMes++;
//
//if (bloqueigMes == 2) {
//
//throw new Exception("Tens un bloqueig a la casella seguent");
//
//}
//
//}
//
//}
//
//}
//} 
//
//if (moviments < 0) {
//for (int i = -1; i >= moviments; i--) {
//bloqueigMenys = 0;
//posObjectiu--;
//for (Jugador comprovar : jugadors) {
//
//if (comprovar.getPos() == posObjectiu) {
//bloqueigMenys++;
//System.out.println("bloqueig : " + bloqueigMenys);
//if (bloqueigMenys == 2) {
//
//throw new Exception("Tens un bloqueig a la casella seguent");
//
//}
//
//}
//
//}
//
//}
//}

		if (!objectiu.equals(actual)) {
			moviments *= -1;
		}

		if (objectiu.isEstaEspecial()) {
			objectiu.setEstaEspecial(false);
		}

		int posActual = objectiu.getPos();
		int posFinal = posActual + moviments;

		if (posFinal >= 0 && posFinal <= taulell.LONGITUD) {
			objectiu.setPos(posFinal);
		} else if (posFinal > taulell.LONGITUD) {
			objectiu.setPos(taulell.LONGITUD);
		} else if (posFinal <= 0) {
			objectiu.setPos(0);
		}

	}

	/*
	 * TELEPORT
	 * 
	 */

	public static void teleport(Jugador actual, Jugador objectiu) {
		int posJugador = actual.getPos();
		int posCanvi = objectiu.getPos();
		actual.setPos(posCanvi);
		objectiu.setPos(posJugador);
	}

	/*
	 * ZANCADILLA
	 * 
	 */

	public static void zancadilla(Jugador objectiu) throws Exception {
		if (objectiu.getZancadilla() != 0) {

			objectiu.setZancadilla(1);

			if (objectiu.teCartes(objectiu.getMa())) {
				int carta = rand.nextInt(objectiu.getMa().size());
				objectiu.getMa().remove(carta);
			} else {
				throw new Exception("El jugador no te cartes.");
			}
		}
	}

	/*
	 * PATADA
	 * 
	 */

	public static void patada(Jugador objectiu) {
		if (objectiu.getPatada() != 0) {
			objectiu.setPatada(1);
			tipusMoviment opcio;
			if (objectiu.teCartes(objectiu.getMa())) {
				for (Carta carta : objectiu.getMa()) {

					opcio = carta.getOpcio();

					if (opcio.equals(tipusMoviment.MOU1)) {

						carta.setOpcio(Carta.tipusMoviment.values()[0]);

					} else if (opcio.equals(tipusMoviment.MOU2)) {

						carta.setOpcio(Carta.tipusMoviment.values()[2]);

					} else if (opcio.equals(tipusMoviment.MOU3)) {

						carta.setOpcio(Carta.tipusMoviment.values()[3]);

					}
				}
			}
		}
	}

	/*
	 * BROMA
	 * 
	 */

	public static void broma(Jugador actual, Jugador objectiu) {
		List<Carta> maActual = actual.getMa();
		List<Carta> maObjectiu = objectiu.getMa();
		actual.setMa(maObjectiu);
		objectiu.setMa(maActual);
	}

	/*
	 * HUNDIMIENTO
	 * 
	 */

	public static void hundimiento(Jugador objectiu) {
		objectiu.setPos(0);
	}

	/*
	 * TREURE PATADA
	 * 
	 */

	public static void treurePatada(Jugador actual) {
		tipusMoviment opcio;

		for (Carta carta : actual.getMa()) {

			opcio = carta.getOpcio();

			if (opcio.equals(tipusMoviment.MOU0)) {

				carta.setOpcio(Carta.tipusMoviment.values()[1]);

			} else if (opcio.equals(tipusMoviment.MOU1)) {

				carta.setOpcio(Carta.tipusMoviment.values()[2]);

			} else if (opcio.equals(tipusMoviment.MOU2)) {

				carta.setOpcio(Carta.tipusMoviment.values()[3]);

			}

		}

	}

	/*
	 * GESTIONAR VIDES
	 * 
	 */

//public void gestionarVides (int numJugador, int vides, Carta carta, Carta.tipusMoviment opcio) throws Exception {
//
//Jugador jugador = null;
//for (Jugador aux : jugadors) {
//if (aux.getId() == numJugador) {
//jugador = aux;
//}
//}
//
//if (vides != 1 && vides != -1) {
//
//throw new Exception("Numero de moviments incorrecte.");
//
//}
//
//jugador.setVides((jugador.getVides() + vides));
//
//
//}

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////	
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
}
