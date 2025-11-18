import { jwtDecode } from "jwt-decode"
import api from "../api"
import { useState, useRef, useEffect } from "react"
import Expenses from "./Expenses"
const GetCategoryByUser = (props) => {
  const [categoris, setCategoris] = useState([])
  const [select, setSelect] = useState(-1)
  const [error, setError] = useState("")
  const sel = useRef()
    const fetch = async () => {
      try{
        if (props.userId > -1 && select === -1){
            const response = await api.get(`/api/cash/categories/${props.userId}`)
            setCategoris(response.data)
            setError("")
            props.tr()
          }
      } catch(err){
        if(err.response.status === 404){
          setError(err.response.data)
        } else{
          setError(err)
        }
      }
    }
    useEffect(() => {
      const fetch = async () => {
        if (select > -1) {
          const response = await api.get(`/api/cash/transactions-by-category/${select}`)
          props.filterData(response.data)
        } else{
          const reponse = await api.get(`/api/cash/items/${jwtDecode(localStorage.getItem("token")).userId}`)
          props.filterData(reponse.data)
        }
      }
      fetch()
    }, [select])
    useEffect(() => {
      sel.current.addEventListener("click", fetch)
    }, [props.userId, select])
  return(
    <div >
      <select ref={sel} onChange={e => setSelect(e.target.value)} value={select} className="roll">
        <option value={-1}>filtering by category</option>
        {
          categoris.map(item => <option value={item.id} key={item.id}>category: {item.category}</option>)
        }
      </select>
      <Expenses userId={props.userId} idCategory={select}/>
      <p>{error}</p>
    </div>
  )
}
export default GetCategoryByUser