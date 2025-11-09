import { useState } from "react"
import api from "../api"

const AddCatTran = (props) => {
  const [data, setData] = useState({category:"", tran:0, description:""})
  const [error, setError] = useState("")
  const handlerData = e => {
    const {name, value} = e.target
    setData(item => ({
      ...item,
      [name]:value
    }))
  }
  const handlerSubmit = async (e) => {
    e.preventDefault()
    try{
      await api.post(`/api/cash/create/${props.id}`, data)
      setError("")
      props.tr()
      setData(item => ({...item, category:"", description:""}))
    } catch(err){
      if(err.response.status === 403){
        setError("No access")
      } else if(err.response.data){
        setError(err.response.data)
      } else{
        setError("An unexpected error occurred")
      }
    }
  }
  return(
    <div>
      <form onSubmit={handlerSubmit}>
        <input type="text" name="category" value={data.category} onChange={handlerData} placeholder="category"/>
        <input type="text" name="description" value={data.description} onChange={handlerData} placeholder="description"/>
        <input type="number" name="tran" step={0.1} min={0} value={data.tran} onChange={handlerData}/>
        <input type="submit" value="ADD" />
      </form>
      <p>{error}</p>
    </div>
  )
}
export default AddCatTran
