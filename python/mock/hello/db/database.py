from db.entity import BaseEntity


class DB:
    def __init__(self) -> None:
        self.data:dict[str,str]= {}

    def findById(self, key:str)->str|None:
        if key not in self.data.keys():
            return None
        return self.data[key]
    
    def put(self,key:str, value:str)->str|None:
        previous:str = self.findById(key)
        self.data[key] = value
        return previous
    
    def delete(self, key:str)-> str|None:
        return self.data.pop(key)
    

class BaseDao:
    def __init__(self,db:DB,type:str) -> None:
        self.db = db
        self.type = type

    def findById(self, id:int) -> BaseEntity|None:
        raw_entity = self.db.findById(self._key(id))
        if not raw_entity :
            return None
        return BaseDao._from_json(raw_entity)
    
    def put(self,entity:BaseEntity) -> BaseEntity|None:
        key = self._key(entity.id)
        content = entity.model_dump_json()
        previous = self.db.put(key,content)
        return BaseDao._from_previous(previous)
    
    def delete(self, id:int) -> BaseEntity|None:
        key = self._key(id)
        previous = self.db.delete(key)
        return BaseDao._from_previous(previous)
    
    def _key(self,id:int)->str:
        return f'{self.type}-{id}'
    
    def _from_json(json:str)->BaseEntity:
        return BaseEntity.model_validate_json(json)
    
    def _from_previous(previous:str|None) -> BaseEntity|None:
        if not previous:
            return None
        return BaseDao._from_json(previous)