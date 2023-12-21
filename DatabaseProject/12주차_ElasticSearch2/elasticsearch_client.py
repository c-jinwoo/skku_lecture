from elasticsearch import Elasticsearch, helpers
from src.configuration import Configuration


class ElasticsearchClient:
    def __init__(self):
        self.config = Configuration().get_config('elasticsearch')
        self.client = self.connect_to_elastic()

    def connect_to_elastic(self) -> Elasticsearch:
        client = Elasticsearch(
            f"{self.config['host']}:{self.config['port']}",
            ca_certs=self.config['ca_cert'],
            basic_auth=(self.config['username'], self.config['password'])
        )
        return client
        
    def delete_index(self, index_name: str) -> None:
        ################# EDIT HERE #################
        try:
            if self.client.indices.exists(index=index_name):
                self.client.indices.delete(index=index_name)
                print(f"Index deleted")    
        except Exception as e:
            print(f"Error : {str(e)}")
        ################# EDIT HERE #################
    
    def create_index(self, index_name: str, mapping: dict) -> None:
        ################# EDIT HERE #################        
        if not self.client.indices.exists(index=index_name):
            self.client.indices.create(index=index_name, body=mapping)            
        ################# EDIT HERE #################

    def insert_one_document(self, index_name:str, body: dict, doc_id=None) -> None:
        ################# EDIT HERE #################        
        response = self.client.index(index=index_name, id=doc_id, body=body)   
        ################# EDIT HERE #################
    
    def delete_document(self, index_name: str, doc_id: int) -> None:
        ################# EDIT HERE #################        
        response = self.client.delete(index=index_name, id=doc_id)        
        ################# EDIT HERE #################

    def bulk_request(self, actions: list = None) -> None:
        ################# EDIT HERE #################
        try:
            res = helpers.bulk(self.client, actions)
        except Exception as e:
            print(f"Error: {str(e)}")
        ################# EDIT HERE #################
    
    def get_document(self, index_name: str, doc_id: int) -> dict:
        ################# EDIT HERE #################
        response = self.client.get(index=index_name, id=doc_id)
        ################# EDIT HERE #################
        return response['_source']
    
    def count(self, index_name: str) -> int:
        self.client.indices.refresh(index=index_name)        
        ################# EDIT HERE #################
        result = self.client.count(index=index_name)
        ################# EDIT HERE #################        
        return result['count']
    
    def search(self, query: dict, index_name: str) -> list:
        ################# EDIT HERE #################
        result = self.client.search(index=index_name, body=query)
        ################# EDIT HERE #################
        return result['hits']['hits']
                
    def scan_index(self, index_name: str, query: dict, size: int, scroll='2m') -> list:
        ################# EDIT HERE #################
        response = helpers.scan(client=self.client, query=query, index=index_name, scroll=scroll, size=size)
        ################# EDIT HERE #################
        for doc in response:
            yield doc['_source']
    
    def update_document_by_id(self, index_name: str, doc_id: int, body: dict) -> None:
        ################# EDIT HERE #################
        response = response = self.client.update(index=index_name, id=doc_id, body={'doc': body})
        ################# EDIT HERE #################
