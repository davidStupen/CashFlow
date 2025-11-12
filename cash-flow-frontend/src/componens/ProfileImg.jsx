import { useEffect, useState } from "react"
import api from "../api"

const ProfileImg = (props) => {
  const [profile, setProfile] = useState("")
  useEffect(() => {
    const fetch = async () => {
      try{
          if(props.userId > -1){
            const response = await api.get(`/api/cash/profile-img/${props.userId}`)
            setProfile(response.data)
          }
      } catch(err){
        console.error(err)
      }
    }
    fetch()
  }, [props.userId])
  if(profile.length > 0){
    return(
      <div>
        <img height={55} src={profile} alt="profile"/>
      </div>
    )
  }
}
export default ProfileImg