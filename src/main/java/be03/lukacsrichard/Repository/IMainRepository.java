package be03.lukacsrichard.Repository;

import java.util.List;

public interface IMainRepository<T> {
    public void Insert(T item);
    public void Update(String symbol, T item);
    public void Delete(String symbol);
    public T GetBySymbol(String symbol);
    public List<T> GetAll();

}
