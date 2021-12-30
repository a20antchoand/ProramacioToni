package FartOS;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

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
	public int muerteSubita = -1;
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




	
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////	
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////	




////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////VALIDACIONS////////////////////////////////////////////////////////////////////////	
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	

	/*
	 * COMPROVAR POSICIO JUGADOR
	 * 
	 */

	public void comprovarPosicio(Jugador objectiu) throws Exception {

		int numJugador;

		if (objectiu.getPos() == CASELLAESPECIAL && !objectiu.isEstaEspecial()) {

			System.out.println("\n" + objectiu.getNom() + ", estas a la casella especial.");
			
			do {

				System.out.print("Indica a quin jugador vols afectar: ");
				numJugador = (s.nextInt());

			} while (FartOSGame.validarJugador(numJugador) == null);

			if (numJugador != objectiu.getId()) {
				objectiu.setEstaEspecial(true);
			}
			
			FartOSGame.afectat = jugadors.get(numJugador-1);
						
		}
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

			resultat.append((j.getId()) + " - NOM: " + j.getNom());
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

					resultat.append(" " + (j.getId()) + " #");

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
