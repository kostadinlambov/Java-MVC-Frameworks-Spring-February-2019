package kl.services;


import kl.domain.entities.Document;
import kl.domain.models.service.DocumentServiceModel;
import kl.repositories.DocumentRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DocumentServiceImpl implements DocumentService {
    private final ModelMapper modelMapper;
    private final DocumentRepository documentRepository;

    @Autowired
    public DocumentServiceImpl(ModelMapper modelMapper, DocumentRepository documentRepository) {
        this.modelMapper = modelMapper;
        this.documentRepository = documentRepository;
    }


    @Override
    public DocumentServiceModel save(DocumentServiceModel documentServiceModel) {
        try {
            Document document = this.modelMapper.map(documentServiceModel, Document.class);
            Document savedDocument = this.documentRepository.saveAndFlush(document);

            return this.modelMapper.map(savedDocument, DocumentServiceModel.class);

        } catch (Exception e) {
            throw new IllegalArgumentException("Something went wrong!");
        }

    }

    @Override
    public DocumentServiceModel findById(String id) {
        Document document = this.documentRepository.findById(id).orElse(null);
        if (document == null) {
            throw new IllegalArgumentException("Document does not exist!");
        }
        return this.modelMapper.map(document, DocumentServiceModel.class);
    }

    @Override
    public List<DocumentServiceModel> allDocuments() {
        List<Document> documentsAll = this.documentRepository.findAll();

        if (documentsAll.size() > 0) {
            return documentsAll.stream().map(document -> this.modelMapper.map(document, DocumentServiceModel.class)).collect(Collectors.toList());
        }

        return null;
    }

    @Override
    public boolean removeDocument(String documentId) {
        Document documentRepositoryById = this.documentRepository.findById(documentId).orElse(null);

        if (documentRepositoryById != null) {
            try{
                this.documentRepository.deleteById(documentId);
                return true;
            }catch (Exception e){
                throw new IllegalArgumentException("Something went wrong by deleting of the document");
            }
        }
        return false;
    }


}
