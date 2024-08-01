package net.minecraft.command;

import java.util.List;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;

public class CommandServerKick extends CommandBase {
   public String getCommandName() {
      return "kick";
   }

   public int getRequiredPermissionLevel() {
      return 3;
   }

   public String getCommandUsage(ICommandSender sender) {
      return "commands.kick.usage";
   }

   public void processCommand(ICommandSender sender, String[] args) throws CommandException {
      if (args.length > 0 && args[0].length() > 1) {
         EntityPlayerMP var3 = MinecraftServer.getServer().getConfigurationManager().getPlayerByUsername(args[0]);
         String var4 = "Kicked by an operator.";
         boolean var5 = false;
         if (var3 == null) {
            throw new PlayerNotFoundException();
         } else {
            if (args.length >= 2) {
               var4 = CommandBase.getChatComponentFromNthArg(sender, args, 1).getUnformattedText();
               var5 = true;
            }

            var3.playerNetServerHandler.kickPlayerFromServer(var4);
            if (var5) {
               CommandBase.notifyOperators(sender, this, "commands.kick.success.reason", var3.getName(), var4);
            } else {
               CommandBase.notifyOperators(sender, this, "commands.kick.success", var3.getName());
            }

         }
      } else {
         throw new WrongUsageException("commands.kick.usage", new Object[0]);
      }
   }

   public List addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
      return args.length >= 1 ? CommandBase.getListOfStringsMatchingLastWord(args, MinecraftServer.getServer().getAllUsernames()) : null;
   }
}
