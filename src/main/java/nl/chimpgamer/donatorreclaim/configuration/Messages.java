package nl.chimpgamer.donatorreclaim.configuration;

import nl.chimpgamer.donatorreclaim.DonatorReclaim;
import nl.chimpgamer.donatorreclaim.utils.FileUtils;
import nl.chimpgamer.donatorreclaim.utils.Utils;

import java.io.IOException;

public class Messages extends FileUtils {
    private final DonatorReclaim donatorReclaim;

    public Messages(DonatorReclaim donatorReclaim) {
        super(donatorReclaim.getDataFolder().getPath(), "messages.yml");
        this.donatorReclaim = donatorReclaim;
    }

    public void load() {
        setupFile();
        for (Message messageKey : Message.values()) {
            this.addDefault(messageKey.getKey(), messageKey.getDefaultValue());
        }
        copyDefaults(true);
        this.save();
    }

    public String getString(Message message) {
        return Utils.formatColorCodes(getString(message.getKey(), message.getDefaultValue()));
    }

    private void setupFile() {
        if (!this.getFile().exists()) {
            try {
                this.saveToFile(donatorReclaim.getResource("messages.yml"));
                this.reload();
            } catch (NullPointerException ex) {
                try {
                    this.getFile().createNewFile();
                } catch (IOException ex1) {
                    ex1.printStackTrace();
                }
            }
        }
    }
}