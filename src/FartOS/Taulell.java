package FartOS;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import FartOS.Carta.tipusMoviment;

public class Taulell {

	// ATRIBUTS FINALS

	public final int LONGITUD = 15;
	public final int CASELLAESPECIAL = 8;
	public final int VALORESPECIAL = 2;

	// ATRIBUTS ESTATICS
	public static Random rand = new Random();
	public static Scanner s = new Scanner(System.in);

	// ATRIBUTS DE TAULELL

	public int numeroJugadors;
	public int[] primerJugador = new int[LONGITUD];
	public int muerteSubita = -1;
	public List<Carta> baralla;
	public List<Jugador> jugadors = new ArrayList<>();

	/*
	 * INICIAR TAULELL
	 * 
	 */

	public Taulell(List<Jugador> jugadors) throws Exception {

		if (jugadors.size() < 3) {
			throw new IllegalArgumentException("Numero de jugadors invalid, minim 3.");
		}

		if (jugadors.size() > 6) {
			throw new IllegalArgumentException("Numero de jugadors invalid, maxim 6.");
		}

		for (int i = 0; i < jugadors.size(); i++) {
			this.jugadors = jugadors;
		}

		this.numeroJugadors = jugadors.size();

	}

	/*
	 * REPARTIR MA
	 * 
	 */

	public void repartirMa(List<Carta> baralla) throws Exception {

		List<Carta> maActual;

		if (numeroJugadors == 3 || numeroJugadors == 4) {

			for (Jugador j : jugadors) {
				maActual = new ArrayList<>();
				for (int i = 0; i < Carta.NUMCARTES34; i++) {

					maActual.add(baralla.get(baralla.size() - 1));
					baralla.remove(baralla.size() - 1);

				}

				j.setMa(maActual);

			}

		} else if (numeroJugadors == 5 || numeroJugadors == 6) {

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

	public void mostrarMa(int idJugador) {

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

	public Carta validarCarta(Jugador actual, int numCarta) {

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

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////	
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////	

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////	
/////////////////////////////////////////////////PODERS DE CARTES///////////////////////////////////////////////////////////////////	
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/*
	 * EXECUTAR
	 * 
	 */

	public void executar(tipusMoviment opcioJugar, Jugador actual, Jugador objectiu) throws Exception {

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

	public void moureJugador(Jugador actual, Jugador objectiu, int moviments) throws Exception {
//		int posObjectiu = objectiu.getPos();
//		int bloqueigMes;
//		int bloqueigMenys;

		if (moviments < -3 && moviments > 3) {

			throw new Exception("Numero de moviments incorrecte.");

		}

//		if (moviments > 0) {
//			for (int i = 1; i <= moviments; i++) {
//				bloqueigMes = 0;
//				bloqueigMenys = 0;
//				for (Jugador comprovar : jugadors) {
//
//					if (comprovar.getPos() == (objectiu.getPos() + i)) {
//						bloqueigMes++;
//
//						if (bloqueigMes == 2) {
//
//							throw new Exception("Tens un bloqueig a la casella seguent");
//
//						}
//
//					}
//
//				}
//
//			}
//		} 
//		
//		if (moviments < 0) {
//			for (int i = -1; i >= moviments; i--) {
//				bloqueigMenys = 0;
//				posObjectiu--;
//				for (Jugador comprovar : jugadors) {
//
//					if (comprovar.getPos() == posObjectiu) {
//						bloqueigMenys++;
//						System.out.println("bloqueig : " + bloqueigMenys);
//						if (bloqueigMenys == 2) {
//
//							throw new Exception("Tens un bloqueig a la casella seguent");
//
//						}
//
//					}
//
//				}
//
//			}
//		}

		if (!objectiu.equals(actual)) {
			moviments = -moviments;
		}

		if (objectiu.isEstaEspecial()) {
			objectiu.setEstaEspecial(false);
		}
		
		int posActual = objectiu.getPos();
		int posFinal = posActual + moviments;

		if (posFinal >= 0 && posFinal <= LONGITUD) {
			objectiu.setPos(posFinal);
		} else if (posFinal > LONGITUD) {
			objectiu.setPos(LONGITUD);
		}

	}

	/*
	 * TELEPORT
	 * 
	 */

	public void teleport(Jugador actual, Jugador objectiu) {
		int posJugador = actual.getPos();
		int posCanvi = objectiu.getPos();
		actual.setPos(posCanvi);
		objectiu.setPos(posJugador);
	}

	/*
	 * ZANCADILLA
	 * 
	 */

	public void zancadilla(Jugador objectiu) throws Exception {
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

	public void patada(Jugador objectiu) {
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

	public void broma(Jugador actual, Jugador objectiu) {
		List<Carta> maActual = actual.getMa();
		List<Carta> maObjectiu = objectiu.getMa();
		actual.setMa(maObjectiu);
		objectiu.setMa(maActual);
	}

	/*
	 * HUNDIMIENTO
	 * 
	 */

	public void hundimiento(Jugador objectiu) {
		objectiu.setPos(0);
	}

	/*
	 * TREURE PATADA
	 * 
	 */

	public void treurePatada(Jugador actual) {
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

//	public void gestionarVides (int numJugador, int vides, Carta carta, Carta.tipusMoviment opcio) throws Exception {
//		
//		Jugador jugador = null;
//		for (Jugador aux : jugadors) {
//			if (aux.getId() == numJugador) {
//				jugador = aux;
//			}
//		}
//		
//		if (vides != 1 && vides != -1) {
//			
//			throw new Exception("Numero de moviments incorrecte.");
//			
//		}
//				 
//		jugador.setVides((jugador.getVides() + vides));
//
//		
//	}

	/*
	 * VALIDAR JUGADOR
	 * 
	 */

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////VALIDACIONS////////////////////////////////////////////////////////////////////////	
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public Jugador validarJugador(int numJugador) {

		Jugador resultat = null;
		if (numJugador >= 0 && numJugador < this.numeroJugadors) {

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

	/*
	 * COMPROVAR POSICIO JUGADOR
	 * 
	 */

	public boolean comprovarPosicio(Jugador actual, Jugador objectiu) throws Exception {

		int numJugador;
		Jugador afectat;

		if (objectiu.getPos() == CASELLAESPECIAL && !objectiu.isEstaEspecial()) {

			System.out.println("\n" + objectiu.getNom() + ", estas a la casella especial.");
			
			do {

				System.out.print("Indica a quin jugador vols afectar: ");
				numJugador = (s.nextInt() - 1);

			} while (validarJugador(numJugador) == null);

			if (numJugador != objectiu.getId()) {
				objectiu.setEstaEspecial(true);
			}
			
			afectat = jugadors.get(numJugador);

			executar(tipusMoviment.MOUESP, objectiu, afectat);
			
			revisarMuerteSubita();
			
		}
		return false;
	}

	/*
	 * COMPROVAR SI HI HA GUANYADOR
	 * 
	 */

	public Jugador guanyador() {

		Jugador guanyador = null;

		for (Jugador j : jugadors) {

			if (j.getPos() == (LONGITUD)) {

				guanyador = j;

			} else if (jugadors.size() == 1) {

				guanyador = j;

			}

		}

		return guanyador;

	}

	public boolean quedenJugadors(int numJugadors) {
		if (numJugadors == 0) {
			return false;
		}
		return true;
	}

	public void eliminarCasella() {
		if (this.muerteSubita != -1) {
			this.muerteSubita++;
		} else {
			this.muerteSubita = 1;
		}

		revisarMuerteSubita();

	}

	public void revisarMuerteSubita() {

		List<Jugador> mortSubita = new ArrayList<>();

		for (int i = 0; i < jugadors.size(); i++) {

			if (jugadors.get(i).getPos() <= this.muerteSubita) {
				mortSubita.add(jugadors.get(i));
			}

		}
		
		for (int i = 0; i < mortSubita.size(); i++) {
			System.out.println("\nEl jugador " + mortSubita.get(i).getNom() + " ha mort per culpa de la mort subita.");

			jugadors.remove(mortSubita.get(i));				
		}
		
		guanyador();
		
	}

	public void eliminarJugador(Jugador eliminar) {
		jugadors.remove(eliminar);		
	}

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////	
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////TOSTRING///////////////////////////////////////////////////////////////////	
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/*
	 * TO STRING
	 * 
	 */

	@Override
	public String toString() {

		StringBuilder resultat = new StringBuilder();
		resultat.append("\n");

		for (Jugador j : jugadors) {

			resultat.append((j.getId() + 1) + " - NOM: " + j.getNom());
			resultat.append("\n");
		}

		resultat.append("\n");
		resultat.append("#");

		for (int i = 0; i < LONGITUD + 1; i++) {
			if (i != CASELLAESPECIAL) {
				resultat.append("===#");
			} else {
				resultat.append("+++#");
			}

		}

		resultat.append("\n");

		for (Jugador j : jugadors) {

			int posicioJugador = j.getPos();
			resultat.append("#");

			for (int i = 0; i < LONGITUD + 1; i++) {

				if (i <= this.muerteSubita) {

					resultat.append("///#");

				} else if (i == posicioJugador) {

					resultat.append(" " + (j.getId() + 1) + " #");

				} else {

					resultat.append("   #");

				}

			}

			resultat.append("\n");

		}

		resultat.append("#");

		for (

				int i = 0; i < LONGITUD + 1; i++) {
			if (i != CASELLAESPECIAL) {
				resultat.append("===#");
			} else {
				resultat.append("+++#");
			}

		}

		resultat.append("\n  ");

		return resultat.toString();

	}

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

}
