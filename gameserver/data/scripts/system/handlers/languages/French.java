/*
 *  This file is part of Zetta-Core Engine <http://www.zetta-core.org>.
 *
 *  Zetta-Core is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published
 *  by the Free Software Foundation, either version 3 of the License,
 *  or (at your option) any later version.
 *
 *  Zetta-Core is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a  copy  of the GNU General Public License
 *  along with Zetta-Core.  If not, see <http://www.gnu.org/licenses/>.
 */
package languages;

import org.openaion.gameserver.utils.i18n.CustomMessageId;
import org.openaion.gameserver.utils.i18n.Language;

/**
 * @author blakawk
 *
 */
public class French extends Language
{
	public French()
	{
		super("fr");
		addSupportedLanguage("fr_FR");
		
		addTranslatedMessage(CustomMessageId.SERVER_REVISION, "Version du serveur : %-6s");
		addTranslatedMessage(CustomMessageId.WELCOME_PREMIUM, "Bienvenue Membre Priviligié sur le serveur %s.\nDéveloppé par l'équipe Jamie.\nRates du serveur :\nExpérience : %d\nQuêtes : %d\nDrops : %d");
		addTranslatedMessage(CustomMessageId.WELCOME_REGULAR, "Bienvenue sur le serveur %s.\nDéveloppé par l'équipe Jamie.\nRates du serveur :\nExpérience : %d\nQuêtes : %d\nDrops : %d");
		addTranslatedMessage(CustomMessageId.WELCOME_BASIC, "Bienvenue sur le serveur %s.\nDéveloppé par l'équipe Jamie.");
		addTranslatedMessage(CustomMessageId.COMMAND_NOT_ENOUGH_RIGHTS, "Vous n'avez pas le droit d'exécuter cette commande.");
		addTranslatedMessage(CustomMessageId.PLAYER_NOT_ONLINE, "Le joueur %s n'est pas en ligne.");
		addTranslatedMessage(CustomMessageId.INTEGER_PARAMETER_REQUIRED, "Le paramètre doit être un nombre.");
		addTranslatedMessage(CustomMessageId.INTEGER_PARAMETERS_ONLY, "Les paramètres doivent être des nombres.");
		addTranslatedMessage(CustomMessageId.SOMETHING_WRONG_HAPPENED, "Quelque chose s'est mal passé.");
		addTranslatedMessage(CustomMessageId.COMMAND_DISABLED, "Cette commande est désactivée.");
		addTranslatedMessage(CustomMessageId.COMMAND_ADD_SYNTAX, "Syntaxe: //add <nom du joueur> <item id> [<quantité>]");
		addTranslatedMessage(CustomMessageId.COMMAND_ADD_ADMIN_SUCCESS, "Objet(s) ajouté(s) à %s avec succès.");
		addTranslatedMessage(CustomMessageId.COMMAND_ADD_PLAYER_SUCCESS, "%s vous a ajouté %d objet(s).");
		addTranslatedMessage(CustomMessageId.COMMAND_ADD_FAILURE, "L'objet %d n'existe pas et/ou ne peut pas être ajouté à %s.");
		addTranslatedMessage(CustomMessageId.COMMAND_ADDDROP_SYNTAX, "Syntaxe: //adddrop <mob id> <item id> <min> <max> <chance>");
		addTranslatedMessage(CustomMessageId.COMMAND_ADDSET_SYNTAX, "Syntaxe: <nom du joueur> <set id>");
		addTranslatedMessage(CustomMessageId.COMMAND_ADDSET_SET_DOES_NOT_EXISTS, "L'ensemble d'armures %d n'existe pas.");
		addTranslatedMessage(CustomMessageId.COMMAND_ADDSET_NOT_ENOUGH_SLOTS, "L'inventaire requiert au moins %d emplacements libres.");
		addTranslatedMessage(CustomMessageId.COMMAND_ADDSET_CANNOT_ADD_ITEM, "L'objet %d ne peut pas être ajouté à %s.");
		addTranslatedMessage(CustomMessageId.COMMAND_ADDSET_ADMIN_SUCCESS, "L'ensemble d'armures %d a été ajouté à %s avec succès.");
		addTranslatedMessage(CustomMessageId.COMMAND_ADDSET_PLAYER_SUCCESS, "%s vous a ajouté un ensemble d'armures.");
		addTranslatedMessage(CustomMessageId.COMMAND_ADDSKILL_SYNTAX, "Syntaxe: //addskill <id de la compétence> <niveau de la compétence>");
		addTranslatedMessage(CustomMessageId.COMMAND_ADDSKILL_ADMIN_SUCCESS, "Vous avez ajouté la compétence %d à %s avec succès.");
		addTranslatedMessage(CustomMessageId.COMMAND_ADDSKILL_PLAYER_SUCCESS, "%s vous a ajouté une nouvelle compétence.");
		addTranslatedMessage(CustomMessageId.COMMAND_ADDTITLE_SYNTAX, "Syntaxe: //addtitle <id du titre> <nom du joueur> [special]");
		addTranslatedMessage(CustomMessageId.COMMAND_ADDTITLE_TITLE_INVALID, "Le titre %d est invalide, il doit être entre 1 et 50.");
		addTranslatedMessage(CustomMessageId.COMMAND_ADDTITLE_CANNOT_ADD_TITLE_TO_ME, "Vous ne pouvez pas vous ajouter le titre %d.");
		addTranslatedMessage(CustomMessageId.COMMAND_ADDTITLE_CANNOT_ADD_TITLE_TO_PLAYER, "Vous ne pouvez pas ajouter le titre %d à %s.");
		addTranslatedMessage(CustomMessageId.COMMAND_ADDTITLE_ADMIN_SUCCESS_ME, "Vous vous êtes ajouté le titre %d avec succès.");
		addTranslatedMessage(CustomMessageId.COMMAND_ADDTITLE_ADMIN_SUCCESS, "Vous avez ajouté le titre %d à %s avec succès.");
		addTranslatedMessage(CustomMessageId.COMMAND_ADDTITLE_PLAYER_SUCCESS, "%s vous a donné le titre %d.");
		addTranslatedMessage(CustomMessageId.COMMAND_SEND_SYNTAX, "Syntaxe: //send <nom de fichier>");
		addTranslatedMessage(CustomMessageId.COMMAND_SEND_MAPPING_NOT_FOUND, "Fichier %s introuvable.");
		addTranslatedMessage(CustomMessageId.COMMAND_SEND_NO_PACKET, "Pas de paquet à envoyer.");
		addTranslatedMessage(CustomMessageId.CHANNEL_WORLD_DISABLED, "Le canal %s est désactivé, merci d'utiliser les canaux %s ou %s selon votre faction.");
		addTranslatedMessage(CustomMessageId.CHANNEL_ALL_DISABLED, "Les canaux sont désactivés.");
		addTranslatedMessage(CustomMessageId.CHANNEL_ALREADY_FIXED, "Vous êtes déjà fixé sur le canal %s.");
		addTranslatedMessage(CustomMessageId.CHANNEL_FIXED, "Votre chat est maintenant fixé sur le canal %s.");
		addTranslatedMessage(CustomMessageId.CHANNEL_NOT_ALLOWED, "Vous n'êtes pas autorisé à parler sur ce canal.");
		addTranslatedMessage(CustomMessageId.CHANNEL_FIXED_BOTH, "Votre chat est maintenant fixé sur les canaux %s et %s.");
		addTranslatedMessage(CustomMessageId.CHANNEL_UNFIX_HELP, "Tapez %s unfix pour libérer votre chat.");
		addTranslatedMessage(CustomMessageId.CHANNEL_NOT_FIXED, "Votre chat n'est fixé sur aucun canal.");
		addTranslatedMessage(CustomMessageId.CHANNEL_FIXED_OTHER, "Votre chat n'est pas fixé sur ce canal, mais sur %s.");
		addTranslatedMessage(CustomMessageId.CHANNEL_RELEASED, "Votre chat a été libéré du canal %s.");
		addTranslatedMessage(CustomMessageId.CHANNEL_RELEASED_BOTH, "Votre chat a été libéré des canaux %s et %s.");
		addTranslatedMessage(CustomMessageId.CHANNEL_BAN_ENDED, "Vous n'êtes plus banni des canaux de discussion.");
		addTranslatedMessage(CustomMessageId.CHANNEL_BAN_ENDED_FOR, "Le joueur %s n'est plus banni des canaux de discussion.");
		addTranslatedMessage(CustomMessageId.CHANNEL_NAME_ASMOS, "Asmodiens");
		addTranslatedMessage(CustomMessageId.CHANNEL_NAME_ELYOS, "Elyséens");
		addTranslatedMessage(CustomMessageId.CHANNEL_NAME_WORLD, "World");
		addTranslatedMessage(CustomMessageId.CHANNEL_NAME_BOTH, "Asmodiens/Elyséens");
		addTranslatedMessage(CustomMessageId.CHANNEL_COMMAND_ASMOS, "asmo");
		addTranslatedMessage(CustomMessageId.CHANNEL_COMMAND_ELYOS, "ely");
		addTranslatedMessage(CustomMessageId.CHANNEL_COMMAND_WORLD, "world");
		addTranslatedMessage(CustomMessageId.CHANNEL_COMMAND_BOTH, "both");
		addTranslatedMessage(CustomMessageId.CHANNEL_BANNED, "Vous ne pouvez pas utiliser les canaux de discussion car %s vous a banni pour la raison suivante: %s, temps restant: %s.");
		addTranslatedMessage(CustomMessageId.COMMAND_MISSING_SKILLS_STIGMAS_ADDED, "%d compétences basiques et %d compétences stigmas vous ont été ajoutées.");
		addTranslatedMessage(CustomMessageId.COMMAND_MISSING_SKILLS_ADDED, "%d compétences basiques vous ont été ajoutées.");
		addTranslatedMessage(CustomMessageId.USER_COMMAND_DOES_NOT_EXIST, "Cette commande joueur n'existe pas.");
		addTranslatedMessage(CustomMessageId.COMMAND_XP_DISABLED, "Votre gain d'XP a été désactivé. Tapez .xpon pour le réactiver.");
		addTranslatedMessage(CustomMessageId.COMMAND_XP_ALREADY_DISABLED, "Votre gain d'XP est déjà désactivé.");
		addTranslatedMessage(CustomMessageId.COMMAND_XP_ENABLED, "Votre gain d'XP a été ré-activé.");
		addTranslatedMessage(CustomMessageId.COMMAND_XP_ALREADY_ENABLED, "Votre gain d'XP est déjà activé.");
		addTranslatedMessage(CustomMessageId.DREDGION_LEVEL_TOO_LOW, "Votre niveau actuel est trop bas pour entrer dans le Dredgion.");
	}
}
