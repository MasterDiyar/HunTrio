package net.diyarnagibaster.huntrio.research;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public class ModAttachments {
    public static final DeferredRegister<AttachmentType<?>> ATTACHMENTS =
            DeferredRegister.create(NeoForgeRegistries.Keys.ATTACHMENT_TYPES, "huntrio");

    public static final java.util.function.Supplier<AttachmentType<ResearchProgress>> RESEARCH_PROGRESS =
            ATTACHMENTS.register("research_progress", () -> AttachmentType.serializable(ResearchProgress::new).build());
}
