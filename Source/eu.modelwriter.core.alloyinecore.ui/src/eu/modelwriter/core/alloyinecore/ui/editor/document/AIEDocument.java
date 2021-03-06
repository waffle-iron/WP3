package eu.modelwriter.core.alloyinecore.ui.editor.document;

import org.eclipse.core.resources.IFile;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.rules.FastPartitioner;
import org.eclipse.ui.part.FileEditorInput;

import eu.modelwriter.core.alloyinecore.ui.editor.partition.IAIEPartitions;
import eu.modelwriter.core.alloyinecore.ui.editor.scanner.AIEPartitionScanner;
import eu.modelwriter.core.alloyinecore.ui.mapping.cs2as.CS2ASMapping;

public class AIEDocument extends Document {
  public static final String EDITOR_ID = "eu.modelwriter.core.alloyinecore.ui.editor";
  private EObject ecoreRoot;
  private IFile iFile;
  private final CS2ASMapping mapping;
  private final FastPartitioner partitioner;

  public AIEDocument() {
    mapping = new CS2ASMapping();
    partitioner = new FastPartitioner(new AIEPartitionScanner(), IAIEPartitions.ALL_PARTITIONS);
    partitioner.connect(this);
    this.setDocumentPartitioner(IAIEPartitions.AIE_PARTITIONING, partitioner);
  }

  public void setEcoreRoot(final EObject ecoreRoot) {
    this.ecoreRoot = ecoreRoot;
  }

  /**
   * 
   * @return current ecore object
   */
  public EObject getEcoreRoot() {
    return ecoreRoot;
  }

  public IFile getiFile() {
    return iFile;
  }

  /**
   * Saves editor input to current ecore file.
   * 
   * @param overwrite
   * @param element
   * 
   * @return true if succeed.
   */
  public EObject saveInEcore(final Object element, final boolean overwrite) {
    if (overwrite) { // Save as
      if (element instanceof FileEditorInput) {
        return mapping.parseAndSave(get(),
            URI.createFileURI(((FileEditorInput) element).getFile().getFullPath().toString()));
      }
    } else { // Save
      return mapping.parseAndSave(get(),
          URI.createPlatformResourceURI(iFile.getFullPath().toString(), true));
    }
    return null;
  }

  public void setFile(final IFile iFile) {
    this.iFile = iFile;
  }
}
